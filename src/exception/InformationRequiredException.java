package exception;

import domein.RequiredElementBedrijfEnum;


import java.util.Collections;
import java.util.Map;


public class InformationRequiredException extends Exception{
    private static final String MESSAGE = "Bedrijf cannot be created because further information is required";
    private Map<RequiredElementBedrijfEnum, String> informationRequired;

    public InformationRequiredException(Map<RequiredElementBedrijfEnum, String> itemsRequired){
        super(MESSAGE);
        informationRequired = itemsRequired;
    }

    public Map<RequiredElementBedrijfEnum, String> getInformationRequired(){
        return Collections.unmodifiableMap(informationRequired);
    }
}