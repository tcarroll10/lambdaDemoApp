package com.company.app.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

import com.company.app.exception.AppFileFindException;
import com.company.app.exception.AppFileMoveException;
import com.company.app.exception.AppFileReadException;
import com.company.app.exception.AppFileWriteException;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

/**
 * Holds information data for a file and provides multiple helper methods for manipulation of filenames, extensions and
 * directories
 *
 * 
 * @since Sep 20, 2017 10:42:50 PM
 *
 */
public class FileMetadata {

    public static final String EMPTY_STRING = "";
    public static final String SPACE = " ";
    public static final String PERIOD = ".";
    public static final String EQUAL = "=";
    public static final String NEWLINE = "\n";
    public static final String NEWLINEANDTAB = "\n\t";
    public static final String FSLASH = "/";
    public static final String UNDERSCORE = "_";
    public static final String EXT = "csv";
    public static final String TMP_EXT = "tmp";
    public static final Joiner EMPTY_JOINER = Joiner.on(EMPTY_STRING);
    public static final Joiner EMPTY_NONULL_JOINER = Joiner.on(EMPTY_STRING).useForNull(EMPTY_STRING);
    public static final Joiner SPACE_JOINER = Joiner.on(SPACE);
    public static final Joiner UNDERSCORE_JOINER = Joiner.on(UNDERSCORE);
    public static final Joiner PERIOD_JOINER = Joiner.on(PERIOD);
    public static final Joiner EQUAL_JOINER = Joiner.on(EQUAL);
    /**
     * directory name where file is found. changes with environment
     */
    private final String directory;
    /**
     * holds file name without extension
     */
    private final String filename;
    /**
     * holds file extension
     */
    private final String extension;
    /**
     * holds temp temporary extension
     */
    private final String tempExtension;

    private FileMetadata(final Builder builder) {
        this.directory = builder.directory;
        this.filename = builder.filename;
        this.extension = builder.extension;
        this.tempExtension = builder.tempExtension;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(final FileMetadata fileMetadata) {
        return new Builder(fileMetadata);
    }

    /**
     * Static helper method that builds a path given a directory, filename and extension.
     * 
     * The method handles different situations, such as directory with or without final path separator; normalizes
     * directory, removes extension and directory if any of these is included in the filename.
     * 
     * @param directory with or without ending path separator
     * @param filename with or without directory and/or extension
     * @param extension
     * @return fully qualified path (directory + filename + extension)
     */
    public static String buildPath(final String directory, final String filename, final String extension) {
        // start with directory
        String str = StringUtils.cleanPath(directory);
        // add slash if necessary
        str = EMPTY_JOINER.join(str,
                (str.lastIndexOf(FSLASH) != str.length() - 1) && str.length() > 0 ? FSLASH : EMPTY_STRING);
        // add filename without extension
        str = EMPTY_JOINER.join(str, StringUtils.stripFilenameExtension(StringUtils.getFilename(filename)));
        // add period and extension
        return EMPTY_JOINER.join(str, PERIOD, extension);
    }

    /**
     * Static helper method to enhance path name by: trimming beg & end spaces and normalizing path
     * 
     * @param directory the input directory name to be cleaned up according to explanation before
     */
    public static String fixPath(final String path) {
        // parse the path and the the corresponding attributes
        String tempPath = StringUtils.trimWhitespace(path); // from beg and end
        tempPath = StringUtils.cleanPath(tempPath); // normalize input path
        return tempPath;
    }

    /**
     * Static helper method to enhance directory name by: trimming beg & end spaces, removing ending path separator if
     * any and normalizing directory name
     * 
     * @param directory the input directory name to be cleaned up according to explanation before
     */
    public static String fixDirectory(final String directory) {
        String tempDir = StringUtils.trimWhitespace(directory); // from beg and end
        tempDir = StringUtils.cleanPath(tempDir); // normalize input path
        // remove ending path separator (FLASH) when there are multiple
        if (tempDir != null && tempDir.length() > 0 && StringUtils.countOccurrencesOf(tempDir, FSLASH) > 1
                && (tempDir.lastIndexOf(FSLASH) == tempDir.length() - 1)) {
            tempDir = tempDir.substring(0, tempDir.length() - 1);
        }
        return tempDir;
    }

    /**
     * Static helper method to enhance filename by trimmng beg & end spaces, normalizing path if any and removing
     * directory and extension if any of this is passed
     * 
     * @param filepath fully qualified path or just a filename with extension
     * @return filename without directory or extension
     */
    public static String filenameWithoutExt(final String filepath) {
        final String tempPath = fixPath(filepath);
        // get filename without extension
        return StringUtils.stripFilenameExtension(StringUtils.getFilename(tempPath));
    }

    /**
     * Finds whether a file exists in a specific directory. The sub-directories are not searched
     * 
     * @param directory string representing the directory to look at. Note that sub-directories are not searched
     * @param filenameWithExt string representing the filename to look for
     * @return the boolean result
     */
    public static boolean findFilenameWithExtInDir(final String directory, final String filenameWithExt) {
        boolean isFound = false;
        try (final Stream<Path> filePathStream = Files.walk(Paths.get(directory), 1)) {
            isFound = filePathStream.filter(Files::isRegularFile)
                    .filter(path -> String.valueOf(path).endsWith(filenameWithExt)).map(Path::toFile).findFirst()
                    .isPresent();
        } catch (final Exception e) {
            final String msg = String.format("Exception while finding a filename [%s] in a directory [%s]",
                    filenameWithExt, directory);
            throw new AppFileFindException(msg, e);
        }
        return isFound;
    }

    /**
     * Static helper method to enhance filename by trimmng beg & end spaces, normalizing path if any and removing
     * directory and leaving extension
     * 
     * @param filepath fully qualified path or just a filename with extension
     * @return filename without directory but with extension
     */
    public static String filenameWithExt(final String filepath) {
        String tempPath = fixPath(filepath);
        // get filename without extension
        tempPath = StringUtils.getFilename(tempPath);
        return tempPath;
    }

    /**
     * Static helper method to extract file extension from a filename
     * 
     * @param filename but accepts a complete path and removes directory paths and file extension
     */
    public static String fileext(final String filename) {
        final String tempPath = fixPath(filename);
        // get extension without the period
        return StringUtils.getFilenameExtension(tempPath);
    }

    /**
     * Static helper method to test whether the passed filename contains an extension
     * 
     * @param filenameWithOrWithoutExt
     * @return true if the passed filename contains an extension, false otherwise
     */
    public static boolean hasExt(final String filenameWithOrWithoutExt) {
        return StringUtils.hasText(fileext(filenameWithOrWithoutExt));
    }

    /**
     * Convert each property from an input List of Strings [each list property element must be formatted as a "key=val"
     * string] into an element of a Map<String:key, String:val> collection. The processing steps are:
     * <p>
     * <ul>
     * <li>1. ignore input list lines whose elements start with "#" because these are just comments</li>
     * <li>2. include only list lines whose elements include at least one "=" character</li>
     * <li>3. parse each list line by making the key as the string at the left of first equal ("=") char and the val as
     * the string at the right of the first equal ("=") character</li>
     * </ul>
     * 
     * @param keyEqualValStringList the input List<String> in which a property is represented as an element formatted as
     *            a "key=val" string
     * @return the output Map<String, String> in which each element's key is a property key the element's val is the
     *         property val
     */
    public static Map<String, String> keyEqualValStringListToKeyValMap(final List<String> keyEqualValStringList) {
        //
        // place each element from the list into an element into a Map<String:key, String:val>. Ignore comment
        // lines (i.e. start with "#") and lines that do not include the "=" character
        return keyEqualValStringList.stream().filter(line -> !line.trim().startsWith("#"))
                .filter(line -> line.contains(EQUAL))
                .flatMap(
                        line -> ImmutableMap
                                .of(Splitter.on(EQUAL).trimResults().limit(2).splitToList(line).get(0),
                                        Splitter.on(EQUAL).trimResults().limit(2).splitToList(line).get(1))
                                .entrySet().stream())
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue(), (o, n) -> n,
                        LinkedHashMap::new));
    }

    /**
     * Read all lines of a file into a list of strings
     * 
     * @param filepath
     * @return
     */
    public static List<String> readFilelinesIntoList(final String filepath) {
        try {
            return Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new AppFileReadException(String.format("Exception while reading a file [%s]", filepath), e);
        }
    }

    /**
     * Persist the list of strings, in which each element is a line into a file
     * 
     * @param filepath string representing the fully qualified file path
     * @param filelines input List<String> in which each element is a filebody line
     */
    public static void writeLinesToFile(final String filepath, final List<String> filelines) {
        try {
            Files.write(Paths.get(filepath), filelines, StandardCharsets.UTF_8);
        } catch (final Exception e) {
            final String msg = String.format(
                    "Exception occurred while persisting a list of lines filebody [%s] to the filepath [%s]", filelines,
                    filepath);
            throw new AppFileWriteException(msg, e);
        }
    }

    /**
     * Move file from one directory to other. The source and target fully qualified filepaths must be passed. If the
     * targetFilepath already exist, it will be overriden. Source and target directories must exist before invoking this
     * function (this function will NOT create any directory)
     * 
     * @param sourceFilepath fully qualified source filepath string
     * @param targetFilepath fully qualified target filepath string
     */
    public static void moveFile(final String sourceFilepath, final String targetFilepath) {
        try {
            Files.move(Paths.get(sourceFilepath), Paths.get(targetFilepath), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) {
            final String msg = String.format("IOException caught while moving sourceFilepath[%s] to targetFilepath[%s]",
                    sourceFilepath, targetFilepath);
            throw new AppFileMoveException(msg, e);
        }
    }

    public static class Builder {

        private String directory = EMPTY_STRING;
        private String filename = EMPTY_STRING;
        private String extension = EXT;
        private String tempExtension = TMP_EXT;

        public Builder() {
            // this constructor is needed. Invoked by the static FileMetadata build method
        }

        public Builder(final FileMetadata fileMetadata) {
            this.directory = fileMetadata.directory;
            this.filename = fileMetadata.filename;
            this.extension = fileMetadata.extension;
            this.tempExtension = fileMetadata.tempExtension;
        }

        /**
         * Enhanced directory setter by trimming beg & end spaces, removing ending path separator if any and normalizing
         * directory name
         * 
         * @param directory
         */
        public Builder directory(final String directory) {
            this.directory = fixDirectory(directory);
            return this;
        }

        /**
         * Enhanced filename setter by trimmng beg & end spaces, normalizing path if any and removing directory and
         * extension if any of this is passed
         * 
         * @param filename but accepts a complete path and removes directory paths and file extension
         */
        public Builder filename(final String filename) {
            this.filename = filenameWithoutExt(filename);
            return this;
        }

        public Builder extension(final String extension) {
            this.extension = extension;
            return this;
        }

        public Builder filenameAndExtension(final String filename) {
            this.filename = filenameWithoutExt(filename);
            this.extension = fileext(filename);
            return this;
        }

        public Builder tempExtension(final String tempExtension) {
            this.tempExtension = tempExtension;
            return this;
        }

        public Builder filenameWithOrWithoutExt(final String filenameWithOrWithoutExt) {
            return FileMetadata.hasExt(filenameWithOrWithoutExt) ? filenameAndExtension(filenameWithOrWithoutExt)
                    : filename(filenameWithOrWithoutExt);
        }

        /**
         * Enhanced setter for a path that does not exist as variable. Interprets the different path components (i.e.
         * directory, filename & extension) and assigns each path to the corresponding instance variable
         * 
         * 
         * @param path fully qualified name of directory + filename + extension
         */
        public Builder path(final String path) {
            final String tempPath = fixPath(path);
            // get filename without extension
            this.filename = StringUtils.stripFilenameExtension(StringUtils.getFilename(tempPath));
            // get extension without the period
            this.extension = StringUtils.getFilenameExtension(tempPath);
            // remove ending filename with extension leaving ending path separator (FLASH)
            String tempDir = StringUtils.delete(tempPath, StringUtils.getFilename(tempPath));
            if (tempDir != null && tempDir.length() > 0 && StringUtils.countOccurrencesOf(tempDir, FSLASH) > 1
                    && (tempDir.lastIndexOf(FSLASH) == tempDir.length() - 1)) {
                tempDir = tempDir.substring(0, tempDir.length() - 1);
            }
            // remove ending path separator (FLASH) when there are multiple
            this.directory = tempDir;
            return this;
        }

        public FileMetadata build() {
            return new FileMetadata(this);
        }
    }

    public String getDirectory() {
        return directory;
    }

    public String getFilename() {
        return filename;
    }

    public String getExtension() {
        return extension;
    }

    public String getTempExtension() {
        return tempExtension;
    }

    /**
     * returns filename with extension but without directory
     * 
     * @return filename (with directory) + extension
     */
    public String getFilenameWithExt() {
        final String str = StringUtils.stripFilenameExtension(StringUtils.getFilename(filename));
        return EMPTY_JOINER.join(str, PERIOD, extension);
    }

    /**
     * returns filename with temporary extension but without directory
     * 
     * @return filename (with directory) + temporary-extension
     */
    public String getFilenameWithTempExt() {
        final String str = StringUtils.stripFilenameExtension(StringUtils.getFilename(filename));
        return EMPTY_JOINER.join(str, PERIOD, tempExtension);
    }

    /**
     * returns full path with temp extension (directory + filename + tempExtension)
     * 
     * @return
     */
    public String getPathWithTempExtension() {
        return EMPTY_JOINER.join(StringUtils.stripFilenameExtension(getPath()), PERIOD, tempExtension);
    }

    /**
     * returns full path with extension (directory + filename + extension)
     * 
     * @return
     */
    public String getPath() {
        final String str = StringUtils.cleanPath(directory);
        return EMPTY_JOINER.join(str,
                (str.lastIndexOf(FSLASH) != str.length() - 1) && str.length() > 0 ? FSLASH : EMPTY_STRING,
                getFilenameWithExt());
    }

    @Override
    public String toString() {
        return "FileMetadata [directory=" + directory + ", filename=" + filename + ", extension=" + extension
                + ", tempExtension=" + tempExtension + "]";
    }

    public String printOutputs() {
        return EMPTY_NONULL_JOINER.join(NEWLINEANDTAB, "FileMetadata:", NEWLINEANDTAB, "getDirectory(): ", "[",
                getDirectory(), "]", NEWLINEANDTAB, "getFilename(): ", "[", getFilename(), "]", NEWLINEANDTAB,
                "getFilenameWithExt(): ", "[", getFilenameWithExt(), "]", NEWLINEANDTAB, "getFilenameWithTempExt(): ",
                "[", getFilenameWithTempExt(), "]", NEWLINEANDTAB, "getExtension(): ", "[", getExtension(), "]",
                NEWLINEANDTAB, "getTempExtension(): ", "[", getTempExtension(), "]", NEWLINEANDTAB, "getPath(): ", "[",
                getPath(), "]", NEWLINEANDTAB, "getPathWithTempExtension(): ", "[", getPathWithTempExtension(), "]",
                NEWLINEANDTAB);
    }
}
