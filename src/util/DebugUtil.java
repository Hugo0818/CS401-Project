package util;

public class DebugUtil {
    public static String getCallerInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int callerIndex = 3;
        if (stackTrace.length <= callerIndex) {
            // Fallback to last available element
            callerIndex = stackTrace.length - 1;
        }
        StackTraceElement caller = stackTrace[callerIndex];
        String className = caller.getClassName();
        String methodName = caller.getMethodName();
        className = className.substring(className.lastIndexOf('.') + 1);
        return "[" + className + "." + methodName + "]";
    }
}
