package ss18.mc.positime.utils;

import ss18.mc.positime.local.BenutzerDatabase;

public final class UtilFunctions {
    private UtilFunctions(){}

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

    public static String calculateSalary(int timeInSec, double moneyperhour){
        Double time = Double.valueOf(UtilFunctions.secondsToTimestring(timeInSec));
        String res =  new Double(time * moneyperhour).toString();
        String cents = res.split("\\.")[1]+"0";
        try {
            cents = cents.substring(0, 2);
        }catch(Exception e){}
        return res.split("\\.")[0] +"."+cents;
    }

}
