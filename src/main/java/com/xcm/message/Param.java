package com.xcm.message;

import java.lang.annotation.*;

/**
 * @author xuecm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.PARAMETER,
})
@Documented
public @interface Param {

    String value();


}
