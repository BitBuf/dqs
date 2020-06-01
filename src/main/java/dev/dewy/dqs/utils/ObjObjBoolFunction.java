package dev.dewy.dqs.utils;

@FunctionalInterface
public interface ObjObjBoolFunction<A, B>
{
    boolean apply(A a, B b);
}