package com.company.app.util;

import static java.time.temporal.ChronoUnit.DAYS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Holds general utility static methods
 *
 * 
 * @since 12/3/2018 9:11 AM
 *
 */
public abstract class Util {

    private static final BigDecimal MAX_GEN_BIGDECIMAL = new BigDecimal("100");
    private static final String EMPTY_STRING = "";

    /**
     * Calculates the number of batches based on the total number of records and batch size value
     *
     * @param totRecords to be split into batches
     * @param batchSize is the maximum number of records per batch
     * @return the total number of batches. When totRecords is greater than the batch size, there will be multiple
     *         batches and all of them excepting the last batch will contain the maximum batch size.
     */
    public static int findNumberOfBatches(final int totRecords, final int batchSize) {
        Preconditions.checkArgument(totRecords >= 0, "totRecords [%s] must be greater than or equal to zero",
                totRecords);
        Preconditions.checkArgument(batchSize > 0, "batchSize [%s] must be greater than zero", batchSize);
        return (totRecords / batchSize) + (totRecords % batchSize == 0 ? 0 : 1);
    }

    public static int findNumberOfFullSizeBatches(final int totRecords, final int batchSize) {
        Preconditions.checkArgument(totRecords >= 0, "totRecords [%s] must be greater than or equal to zero",
                totRecords);
        Preconditions.checkArgument(batchSize > 0, "batchSize [%s] must be greater than zero", batchSize);
        return (totRecords / batchSize);
    }

    public static int findNumberOfPartialSizeBatches(final int totRecords, final int batchSize) {
        Preconditions.checkArgument(totRecords >= 0, "totRecords [%s] must be greater than or equal to zero",
                totRecords);
        Preconditions.checkArgument(batchSize > 0, "batchSize [%s] must be greater than zero", batchSize);
        return (totRecords % batchSize == 0 ? 0 : 1);
    }

    public static int findNumberOfRecordsOfPartialSizeBatch(final int totRecords, final int batchSize) {
        Preconditions.checkArgument(totRecords >= 0, "totRecords [%s] must be greater than or equal to zero",
                totRecords);
        Preconditions.checkArgument(batchSize > 0, "batchSize [%s] must be greater than zero", batchSize);
        return totRecords % batchSize;
    }

    /**
     * Generate random BigDecimal amount with two decimal numbers and between 0 and 1000000000
     *
     * @return generated random number
     */
    public static BigDecimal randomBigDecAmt() {
        return randomBigDecByScaleInClosedRange(BigDecimal.ZERO, MAX_GEN_BIGDECIMAL, 2);
    }

    public static BigDecimal randomBigDecAmt(final int min, final int max) {
        return randomBigDecByScaleInClosedRange(min, max, 2);
    }

    public static BigDecimal randomBigDecAmt(final BigDecimal min, final BigDecimal max) {
        return randomBigDecByScaleInClosedRange(min, max, 2);
    }

    /**
     * Generate random BigDecimal rounded to a given decimal scale and with amount between a range of given BigDecimal
     * numbers
     *
     * @param min minimum BigDecimal number to generate
     * @param max maximum BigDecimal number to generate
     * @param scale to round the generated number
     * @return generated random number
     */
    public static BigDecimal randomBigDecByScaleInClosedRange(final int min, final int max, final int scale) {
        return new BigDecimal(String.valueOf(min + (Math.random() * (max - min + 1)))).setScale(scale,
                RoundingMode.HALF_UP);
    }

    /**
     * Generate random BigDecimal rounded to a given decimal scale and with amount between a range of given BigDecimal
     * numbers
     *
     * @param min minimum BigDecimal number to generate
     * @param max maximum BigDecimal number to generate
     * @param scale to round the generated number
     * @return generated random number
     */
    public static BigDecimal randomBigDecByScaleInClosedRange(final BigDecimal min, final BigDecimal max,
            final int scale) {
        return min.add(new BigDecimal(Math.random()).multiply(max.subtract(min).add(BigDecimal.ONE))).setScale(scale,
                BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Generate random LocalDate between two years
     * 
     * @param minYear
     * @param maxYear
     * @return random LocalDate
     */
    public static LocalDate randomLocalDateInClosedRange(final int minYear, final int maxYear) {
        final int day = randomIntNumberInClosedRange(1, 28);
        final int month = randomIntNumberInClosedRange(1, 12);
        final int year = randomIntNumberInClosedRange(minYear, maxYear);
        return LocalDate.of(year, month, day);
    }

    /**
     * Generate random LocalDate between two local dates
     * 
     * @param minDate
     * @param maxDate
     * @return random LocalDate
     */
    public static LocalDate randomLocalDateInClosedRange(final LocalDate minDate, final LocalDate maxDate) {
        final int daysToAdd = (Util.randomBigDecAmt(0, 1).multiply(new BigDecimal(DAYS.between(minDate, maxDate) / 2)))
                .setScale(0, RoundingMode.DOWN).intValueExact();
        return minDate.plusDays(daysToAdd);
    }

    /**
     * Generate an integer random number between a two given numbers (inclusive both); in other words generate a integer
     * random number in a closed range
     *
     * @param min number in the range inclusive
     * @param max number in the range inclusive
     * @return random integer umber
     */
    public static int randomIntNumberInClosedRange(final int min, final int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + new Random().nextInt(max - min + 1);
    }

    /**
     * Find a random element of the passed list
     * 
     * @parm <T> generic type of the passed list element
     * @param list of elements of type T
     * @return a random element of the list
     */
    public static <T> T findRandomElement(final List<T> list) {
        return list.get(randomIntNumberInClosedRange(0, Math.max(0, list.size() - 1)));
    }

    public static String formatWithCommas(final int number) {
        return String.format("%,d", number);
    }

    public static String formatWithCommas(final long number) {
        return String.format("%,d", number);
    }

    /**
     * Format a given number of milliseconds as a String indicating the number of hours, minutes, seconds and
     * milliseconds
     *
     * @param millis number of milliseconds
     * @return formatted string as indicated below
     */
    public static String formatMilliSeconds(final long millis) {
        String str = EMPTY_STRING;
        final long days = TimeUnit.MILLISECONDS.toDays(millis);
        final long hours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(days);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.DAYS.toMinutes(days)
                - TimeUnit.HOURS.toMinutes(hours);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.DAYS.toSeconds(days)
                - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);
        final long milliseconds = millis - TimeUnit.HOURS.toMillis(hours) - TimeUnit.DAYS.toMillis(days)
                - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds);
        if (days > 0) {
            str = String.format("%d day, %d hour, %d min, %d sec, %d milsec", days, hours, minutes, seconds,
                    milliseconds);
        } else if (hours > 0) {
            str = String.format("%d hour, %d min, %d sec, %d milsec", hours, minutes, seconds, milliseconds);
        } else if (minutes > 0) {
            str = String.format("%d min, %d sec, %d milsec", minutes, seconds, milliseconds);
        } else if (seconds > 0) {
            str = String.format("%d sec, %d milsec", seconds, milliseconds);
        } else {
            str = String.format("%d milsec", milliseconds);
        }
        return str;
    }

    /**
     * Create a list holding all surrounded-by-parenthesis tokens
     *
     * @param inStr input text such as "(Z230) || ((X241) && (X242)) || (F232)"
     * @return the list of tokens, such as ["Z230", "X241", "X242", "F232"]
     */
    public static List<String> extractListOfTokensEnclosedByParenthesis(final String inStr) {
        final Pattern p = Pattern.compile("\\((?<content>[^()]+)\\)");
        final Matcher m = p.matcher(inStr);
        final List<String> outList = Lists.newArrayList();
        while (m.find()) {
            outList.add(m.group("content").trim());
        }
        return outList;
    }

    /**
     * Create a set holding all surrounded-by-parenthesis tokens
     *
     * @param inStr input text such as "(Z230) || ((X241) && (X242)) || (F232)"
     * @return the set of tokens, such as ["Z230", "X241", "X242", "F232"]
     */
    public static Set<String> extractSetOfTokensEnclosedByParenthesis(final String inStr) {
        final Pattern p = Pattern.compile("\\((?<content>[^()]+)\\)");
        final Matcher m = p.matcher(inStr);
        final Set<String> outSet = Sets.newLinkedHashSet();
        while (m.find()) {
            outSet.add(m.group("content").trim());
        }
        return outSet;
    }

    /**
     * Returns a string representation of the passed map. Replaces the java.util.Map.toString() method. The difference
     * is that this method creates each key,value pair in a separate line; while the java.util.Map.toString method
     * produces all the string representation in one line.
     * <p/>
     * This method will be used for logging large Maps such as those holding all rows of reference table
     * 
     * @param map object instance
     * @return a string representation of the passed map
     */
    public static String mapToString(final Map<?, ?> map) {
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<?, ?> e : map.entrySet()) {
            sb.append("\n\tKey: ").append(e.getKey()).append(", \t\tValue: ").append(e.getValue());
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of the passed list. Replaces the java.util.Map.toString() method. The difference
     * is that this method creates each list element in a separate line rather in on line
     * <p/>
     * This method is useful to print elements, which are objects with multiple instance variables
     * 
     * @param list object instance
     * @return a string representation of the passed map
     */
    public static <T> String listToString(final List<T> list) {
        final StringBuilder sb = new StringBuilder();
        int count = 0;
        if (list != null) {
            sb.append("\n");
            for (final T e : list) {
                sb.append("\n\t").append("#").append(++count).append(": ").append(e);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Replaces all occurrences of "{key}" tokens in a raw url with the "value" in the inMap corresponding to the key in
     * the url and inMap
     * 
     * @param inUrl input raw url. Raw means that contains "{key}" tokens to be expanded as the corresponding "value"
     *            found in the inMap
     * @param inMap input Map<String, Object> data instance that contains key-value pairs used to expand the raw url
     * @return the expanded url with all "{key}" tokens replaced by their "value"s
     */
    public static String cookUrl(final String inUrl, final Map<String, Object> inMap) {
        String expandedUrl = inUrl;
        for (final Entry<String, Object> paramEntry : inMap.entrySet()) {
            // use the inMap key-value pairs to replace each rawUrl's "{key}" token by its "value"
            expandedUrl = StringUtils.replace(expandedUrl,
                    Const.EMPTY_JOINER.join("{", StringUtils.trimWhitespace(paramEntry.getKey()), "}"),
                    StringUtils.trimWhitespace(String.valueOf(paramEntry.getValue())));
        }
        return expandedUrl;
    }

    /**
     * Removes from the passed String all elements of passed tokens array
     * 
     * @param str original string
     * @param tokens list whose elements must be removed from passed string
     * @return passed string with tokens removed
     */
    public static String removeTokens(String str, final List<String> tokens) {
        if (str != null) {
            for (final String token : tokens) {
                str = str.replace(token, EMPTY_STRING);
            }
        }
        return str;
    }

    /**
     * Removes from the passed string all instances of begToken + surrounded string + endToken. This method assumes that
     * the all begTokens are equal to each other and all endTokens are equal to each other; but that each begToken must
     * be different from an endToken
     * 
     * @param str passed string that might contain multiple pairs of begToken and endToken strings surrounding a
     *            delimited substring
     * @param begToken marker string.
     * @param endToken marker string.
     * @return passed string without tokens and surrounding substrings.
     */
    public static String removeTokenPairAndDelimetedStr(String str, final String begToken, final String endToken) {
        if (str != null) {
            // a [begToken, endToken] pair can occur multiple times
            while (str.indexOf(begToken) >= 0) {
                // identify "begToken + surrounded string + endToken" string
                final String begTokenPlusDelimetedStrPlusEndToken = str.substring(str.indexOf(begToken),
                        str.indexOf(endToken) + endToken.length());
                // remove "begToken + surrounded string + endToken" string
                str = str.replace(begTokenPlusDelimetedStrPlusEndToken, EMPTY_STRING);
            }
        }
        return str;
    }

    /**
     * Removes from the passed string all instances of begToken + surrounded string + endToken. This method assumes that
     * the all begTokens are equal to each other and all endTokens are equal to each other; but that each begToken must
     * be different from an endToken
     * 
     * @param str passed string that might contain multiple pairs of begToken and endToken strings surrounding a
     *            delimited substring
     * @param tokens two-size list of strings holding: begToken and endToken
     * @return passed string without tokens and surrounded substring
     */
    public static String removeTokenPairAndDelimetedStr(final String str, final List<String> tokens) {
        return removeTokenPairAndDelimetedStr(str, tokens.get(0), tokens.get(1));
    }

    /**
     * Replaces, for the passed string, all instances of begToken + surrounded string + endToken with a new string. This
     * method assumes that the all begTokens are equal to each other and all endTokens are equal to each other; but that
     * each begToken must be different from an endToken
     * 
     * @param str passed string that might contain multiple pairs of begToken and endToken strings surrounding a
     *            delimited substring
     * @param begToken marker string.
     * @param endToken marker string.
     * @param new string that will replace the beg and end Token pair plus the string enclosed between them
     * @return passed string without tokens and surrounded substring
     */
    public static String replaceTokenPairAndDelimetedStr(String str, final String begToken, final String endToken,
            final String newStr) {
        if (str != null) {
            // a [begToken, endToken] pair can occur multiple times
            while (str.indexOf(begToken) >= 0) {
                // identify "begToken + surrounded string + endToken" string
                final String begTokenPlusDelimetedStrPlusEndToken = str.substring(str.indexOf(begToken),
                        str.indexOf(endToken) + endToken.length());
                // replace "begToken + surrounded string + endToken" string
                str = str.replace(begTokenPlusDelimetedStrPlusEndToken, newStr);
            }
        }
        return str;
    }
}
