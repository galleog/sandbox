package com.github.galleog.sandbox.constants;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.FieldSignature;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Aspect to resolve {@code public static final} fields from the database automatically.
 *
 * @author Oleg Galkin
 */
@Aspect
public class ConstantResolverAspect {
    private FieldResolver resolver;

    /**
     * Sets {@link FieldResolver} to get field values.
     */
    public void setResolver(FieldResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Matches references to any {@code public static final} field annotated with {@link ResolveConstant}.
     */
    @Pointcut("get(@com.github.galleog.sandbox.constants.ResolveConstant public static final * *.*)")
    void getPublicStaticFinalResolveConstantField() {
    }

    /**
     * Matches references to any {@code public static final} field belonging to a class
     * annotated with {@link ResolveConstant}.
     */
    @Pointcut("get(public static final * (@com.github.galleog.sandbox.constants.ResolveConstant *).*)")
    void getAnyPublicStaticFinalFieldOfResolveContstantType() {
    }

    /**
     * Matches a call of {@code Field#get(Object)} for any {@code public static final} field
     * annotated with {@link ResolveConstant}.
     */
    @Pointcut("call(public Object java.lang.reflect.Field.get(Object)) && target(field) && if()")
    public static boolean getField(Field field) {
        int mod = field.getModifiers();
        return Modifier.isPublic(mod) && Modifier.isStatic(mod) && Modifier.isFinal(mod) &&
                (field.isAnnotationPresent(ResolveConstant.class) ||
                        field.getDeclaringClass().isAnnotationPresent(ResolveConstant.class));
    }

    /**
     * Resolves a directly referenced field.
     */
    @Around("getPublicStaticFinalResolveConstantField() || getAnyPublicStaticFinalFieldOfResolveContstantType()")
    public Object resolveDirectConstant(JoinPoint thisJointPoint) {
        FieldSignature signature = (FieldSignature) thisJointPoint.getStaticPart().getSignature();
        return resolver.resolve(signature.getField());
    }

    /**
     * Resolves a field referenced using Reflection.
     */
    @Around("getField(field) && !cflow(within(com.github.galleog.sandbox.constants.ConstantResolverAspect))")
    public Object resolveReflectiveConstant(Field field) {
        return resolver.resolve(field);
    }
}
