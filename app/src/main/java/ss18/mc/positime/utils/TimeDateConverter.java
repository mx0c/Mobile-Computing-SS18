package ss18.mc.positime.utils;

public final class TimeDateConverter {
    private TimeDateConverter(){}

    public static String secondsToTimestring(int seconds){
        Integer minutes = seconds/60;
        Integer hours = minutes/60;
        minutes = minutes % 60;
        if(minutes.toString().length() == 1){
            minutes = Integer.valueOf("0"+minutes.toString());
        }
        if(hours.toString().length() == 1){
            hours = Integer.valueOf("0"+hours.toString());
        }
        return hours.toString() +  "." + minutes.toString();
    }

}
