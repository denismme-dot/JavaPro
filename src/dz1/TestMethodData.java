package dz1;

import java.lang.reflect.Method;

record TestMethodData(Method method, String name, int priority, boolean ignored, int order) {
}