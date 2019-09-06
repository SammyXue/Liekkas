package com.xcm.message;

import java.lang.annotation.*;

/**
 * @author xuecm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,
})
@Documented
public @interface Path {
    Command command();
}
