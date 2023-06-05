package ru.pad;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    /**
     * Регулярное выражение для определения корректности формата электронной почты
     */
    public static final Pattern VALID_EMAIL_REGEX =
            Pattern.compile("^[A-Z\\d._%+-]+@[A-Z\\d.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Определяет, корректный ли формат имеет электронная почта, используя регулярное выражение
     * <b>VALID_EMAIL_REGEX</b>
     * @param email электронная почта, формат которой нужно проверить
     * @return true, если формат электронной почты корректен, иначе - false
     * @see #VALID_EMAIL_REGEX
     */
    public static boolean emailFormatIsValid(String email) {
        Matcher matcher = VALID_EMAIL_REGEX.matcher(email);
        return matcher.matches();
    }
}
