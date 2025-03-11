package com.ducnh.excellentPdf.utils;

import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ducnh.excellentPdf.config.RuntimePathConfig;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileMonitor {
	
	private final Map<Path, WatchKey> path2KeyMapping;
	private final Set<Path> newlyDiscoveredFiles;
	private final ConcurrentHashMap.KeySetView<Path, Boolean> readyForProcessingFiles;
	private final WatchService watchService;
	private final Predicate<Path> pathFilter;
	private final Path rootDir;
	private Set<Path> stagingFiles;
	
	@Autowired
	public FileMonitor(
			@Qualifier("directoryFilter") Predicate<Path> pathFilter,
			RuntimePathConfig runtimePathConfig) throws IOException {
		this.newlyDiscoveredFiles = new HashSet<>();
		this.path2KeyMapping = new HashMap<>();
		this.stagingFiles = new HashSet<>();
		this.pathFilter = pathFilter;
		this.readyForProcessingFiles = ConcurrentHashMap.newKeySet();
		this.watchService = FileSystems.getDefault().newWatchService();
		log.info("Monitoring directory: {}", runtimePathConfig.getPipelineWatchedFoldersPath());
		this.rootDir = Path.of(runtimePathConfig.getPipelineWatchedFoldersPath());
	}
	
	private boolean shouldNotProcess(Path path) {
		return !pathFilter.test(path);
	}
	
	private void recursivelyRegisterEntry(Path dir) throws IOException {
		WatchKey key = dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		path2KeyMapping.put(dir, key);
		log.info("Registered directory: {}", dir);
		
		try (Stream<Path> directoryVisitor = Files.walk(dir, 1)) {
			final Iterator<Path> iterator = directoryVisitor.iterator();
			while (iterator.hasNext()) {
				Path path = iterator.next();
				if (path.equals(dir) || shouldNotProcess(path)) continue;
				
				if (Files.isDirectory(path)) {
					recursivelyRegisterEntry(path);
				} else if (Files.isRegularFile(path)) {
					handleFileCreation(path);
				}
			}
		}
	}
	
	@Scheduled(fixedRate = 5000)
	public void trackFiles() {
		stagingFiles = new HashSet<>(newlyDiscoveredFiles);
		readyForProcessingFiles.clear();
		
		if (path2KeyMapping.isEmpty()) {
			log.warn("not monitoring any directory, even the root directory itself: {}", rootDir);
			if (Files.exists(rootDir)) {
				try {
					recursivelyRegisterEntry(rootDir);
				} catch (IOException e) {
					log.error("unable to register monitoring", e);
				}
			}
		}
		
		WatchKey key;
		while ((key = watchService.poll()) != null) {
			final Path watchingDir = (Path) key.watchable();
			key.pollEvents()
					.forEach(
							(evt) -> {
								final Path path = (Path) evt.context();
								final WatchEvent.Kind<?> kind = evt.kind();
								if (shouldNotProcess(path)) return;
								
								try {
									if (Files.isDirectory(path)) {
										if (kind == ENTRY_CREATE) {
											handleDirectoryCreation(path);
										}
									}
									Path relativePathFromRoot = watchingDir.resolve(path);
									if (kind == ENTRY_CREATE) {
										handleFileCreation(relativePathFromRoot);
									} else if (kind == ENTRY_DELETE) {
										handleFileRemoval(relativePathFromRoot);
									} else if (kind == ENTRY_MODIFY) {
										handleFileModification(relativePathFromRoot);
									}
								} catch (Exception e) {
									log.error("Error while processing file: {}", path, e);
								}
							});
			boolean isKeyValid = key.reset();
			if (!isKeyValid) {
				path2KeyMapping.remove((Path) key.watchable()); 
			}
		}
		readyForProcessingFiles.addAll(stagingFiles);
	}
	
	private void handleDirectoryCreation(Path dir) throws IOException {
		WatchKey key = dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		path2KeyMapping.put(dir, key);
	}
	
	private void handleFileRemoval(Path path) {
		newlyDiscoveredFiles.remove(path);
		stagingFiles.remove(path);
	}
	
	private void handleFileCreation(Path path) {
		newlyDiscoveredFiles.add(path);
		stagingFiles.remove(path);
	}
	
	private void handleFileModification(Path path) {
		handleFileCreation(path);
	}
	
	public boolean isFileReadyForProcessing(Path path) {
		// 1. Check FileMonitor's ready list
		boolean isReady = readyForProcessingFiles.contains(path.toAbsolutePath());
		
		// 2. Check last modified timestamp
		if (!isReady) {
			try {
				long lastModified = Files.getLastModifiedTime(path).toMillis();
				long currentTime = System.currentTimeMillis();
				isReady = (currentTime - lastModified) > 5000;
			} catch (IOException e) {
				log.info("Timestamp check failed for {}", path, e);
			}
		}
		
		// 3. Direct file lock check
		if (isReady) {
			try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "rw");
					FileChannel channel = raf.getChannel()) {
				// Try accquiring an exclusive lock
				FileLock lock = channel.tryLock();
				if (lock == null) {
					isReady = false;
				} else {
					lock.release();
				}
			} catch (IOException e) {
				log.info("File lock detected on {}", path);
				isReady = false;
			}
		}
		return isReady;
	}
}
