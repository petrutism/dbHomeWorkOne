package lt.code.academy.tools;

import com.diogonunes.jcolor.AnsiFormat;

import static com.diogonunes.jcolor.Attribute.*;

public class Print {
    private static final AnsiFormat fError = new AnsiFormat(RED_TEXT(), BOLD());
    private static final AnsiFormat fSuccess = new AnsiFormat(GREEN_TEXT());
    private static final AnsiFormat fAlert = new AnsiFormat(BRIGHT_YELLOW_TEXT());

    public static void pSuccess(String text){
        System.out.println(fSuccess.format(text));
    }
    public static void pAlert(String text){
        System.out.println(fAlert.format(text));
    }
    public static void pError(String text){
        System.out.println(fError.format(text));
    }
}
