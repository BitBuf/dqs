package dev.dewy.dqs.utils;

public class RegexUtils
{
    public static boolean isWhisperTo(String s)
    {
        return s.matches("to .*: .*$");
    }

    public static boolean isWhisperFrom(String s)
    {
        return s.matches("^.* whispers: .*$");
    }
}
