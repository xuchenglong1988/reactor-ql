package org.jetlinks.reactor.ql.utils;

import java.time.*;
import java.util.Date;
import java.util.Objects;

public class CompareUtils {


    public static int compare(Object source, Object target) {
        if (Objects.equals(source, target)) {
            return 0;
        }

        if (source == null || target == null) {
            return -1;
        }

        if (source.equals(target)) {
            return 0;
        }

        if (source.getClass() == target.getClass() && source instanceof Comparable) {
            return ((Comparable) source).compareTo(target);
        }

        //时间
        {
            if (source instanceof Instant) {
                source = Date.from(((Instant) source));
            }
            if (target instanceof Instant) {
                target = Date.from(((Instant) target));
            }

            if (source instanceof LocalDateTime) {
                source = Date.from(((LocalDateTime) source).atZone(ZoneId.systemDefault()).toInstant());
            }
            if (target instanceof LocalDateTime) {
                target = Date.from(((LocalDateTime) target).atZone(ZoneId.systemDefault()).toInstant());
            }
            if (source instanceof LocalDate) {
                source = Date.from(((LocalDate) source).atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            if (target instanceof LocalDate) {
                target = Date.from(((LocalDate) target).atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            if (source instanceof LocalTime) {
                source = Date.from((LocalDateTime.of(LocalDate.now(), ((LocalTime) source))).atZone(ZoneId.systemDefault()).toInstant());
            }
            if (target instanceof LocalTime) {
                target = Date.from((LocalDateTime.of(LocalDate.now(), ((LocalTime) target))).atZone(ZoneId.systemDefault()).toInstant());
            }

            if (source instanceof Date) {
                return compare(((Date) source), target);
            }

            if (target instanceof Date) {
                return -compare(((Date) target), source);
            }
        }

        //枚举
        if (source.getClass().isEnum()) {
            return compare(((Enum<?>) source), target);
        }

        if (target.getClass().isEnum()) {
            return -compare(((Enum<?>) target), source);
        }

        //数字
        {
            if (source instanceof Number) {
                return compare(((Number) source), target);
            }
            if (target instanceof Number) {
                return -compare(((Number) target), source);
            }
        }
        if (source instanceof CharSequence) {
            return compare(String.valueOf(source), target);
        }
        //字符
        if (target instanceof CharSequence) {
            return -compare(String.valueOf(target), source);
        }

        //boolean
        if (source instanceof Boolean
                || target instanceof Boolean) {
            return CastUtils.castBoolean(target) == CastUtils.castBoolean(source)
                    ? 0
                    : -1;
        }

        return -1;
    }

    public static boolean equals(Object source, Object target) {
        try {
            return compare(source, target) == 0;
        } catch (Throwable e) {
            return false;
        }
    }

    private static int compare(Number number, Object target) {
        return Double.compare(number.doubleValue(), CastUtils.castNumber(target).doubleValue());
    }

    private static int compare(Enum<?> e, Object target) {
        if (target instanceof Number) {
            return Integer.compare(e.ordinal(), ((Number) target).intValue());
        }
        return e.name().compareToIgnoreCase(String.valueOf(target));
    }

    private static int compare(String string, Object target) {
        return string.compareTo(String.valueOf(target));
    }

    private static int compare(Date date, Object target) {
        try {
            return date.compareTo(CastUtils.castDate(target));
        } catch (Exception ignore) {
            return -1;
        }
    }


}
