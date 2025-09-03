package exception;

import domein.RequiredElementProductEnum;


import java.util.Collections;
import java.util.Map;


public class ProductInformationRequiredException extends Exception{
    private static final String MESSAGE = "Product cannot be created because further information is required";
    private Map<RequiredElementProductEnum, String> informationRequired;

    public ProductInformationRequiredException(Map<RequiredElementProductEnum, String> itemsRequired){
        super(MESSAGE);
        informationRequired = itemsRequired;
    }

    public Map<RequiredElementProductEnum, String> getInformationRequired(){
        return Collections.unmodifiableMap(informationRequired);
    }
}