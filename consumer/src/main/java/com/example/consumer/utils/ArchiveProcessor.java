package com.example.consumer.utils;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ArchiveProcessor {

    public static final int THREAD_POOL_SIZE = 5;


    public static List<File> extractFiles(File archive) throws IOException, RarException {
        String extension = getFileExtension(archive);
        Path outputDir = Files.createTempDirectory("extracted_");
        List<File> extractedFiles = new ArrayList<>();

        switch (extension) {
            case "zip":
                extractedFiles.addAll(extractZip(archive, outputDir));
                break;
// Deprecated library only 4.x version of rar archives
//            case "rar":
//                extractedFiles.addAll(extractRar(archive, outputDir));
//                break;
            case "7z":
                extractedFiles.addAll(extract7z(archive, outputDir));
                break;
            default:
                throw new IllegalArgumentException("Unsupported archive format: " + extension);
        }

        return extractedFiles;
    }

    private static List<File> extractZip(File archive, Path outputDir) throws IOException {
        List<File> extractedFiles = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(archive)) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    File outFile = new File(outputDir.toFile(), entry.getName());
                    try (InputStream is = zipFile.getInputStream(entry);
                         FileOutputStream fos = new FileOutputStream(outFile)) {
                        is.transferTo(fos);
                    }
                    extractedFiles.add(outFile);
                }
            }
        }
        return extractedFiles;
    }

//    private static List<File> extractRar(File archive, Path outputDir) throws IOException, RarException {
//        List<File> extractedFiles = new ArrayList<>();
//        try (Archive rarArchive = new Archive(new FileInputStream(archive))) {
//            for (FileHeader header : rarArchive.getFileHeaders()) {
//                if (!header.isDirectory()) {
//                    File outFile = new File(outputDir.toFile(), header.getFileNameString().trim());
//                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
//                        rarArchive.extractFile(header, fos);
//                    }
//                    extractedFiles.add(outFile);
//                }
//            }
//        }
//        return extractedFiles;
//    }

    private static List<File> extract7z(File archive, Path outputDir) throws IOException {
        List<File> extractedFiles = new ArrayList<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(archive, "r");
             IInArchive inArchive = SevenZip.openInArchive(ArchiveFormat.SEVEN_ZIP,
                     new RandomAccessFileInStream(randomAccessFile))) {

            List<ISimpleInArchiveItem> archiveItems = List.of(inArchive.getSimpleInterface().getArchiveItems());

            for (ISimpleInArchiveItem item : archiveItems) {
                if (!item.isFolder()) {
                    File outFile = new File(outputDir.toFile(), item.getPath());
                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        ExtractOperationResult result = item.extractSlow(data -> {
                            try {
                                fos.write(data);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return data.length;
                        });
                        if (result != ExtractOperationResult.OK) {
                            throw new IOException("Ошибка извлечения: " + result);
                        }
                    }
                    extractedFiles.add(outFile);
                }
            }
        }
        return extractedFiles;
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        return (lastIndex == -1) ? "" : name.substring(lastIndex + 1).toLowerCase();
    }
}

