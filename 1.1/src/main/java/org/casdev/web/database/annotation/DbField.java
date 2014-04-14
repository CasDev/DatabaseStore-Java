package org.casdev.web.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DbField {
	public String field();
	
	public String map() default "";

	public boolean auto_increment() default false;
	
	public String table() default "";
	
	public String join() default "";
	
	public String joinon() default "";
	
	public boolean load() default false;
	
	public Class clazz() default String.class;
	
	public String value() default "";
	
	public String connect() default "";
	
	public boolean noUpdate() default false;
}
