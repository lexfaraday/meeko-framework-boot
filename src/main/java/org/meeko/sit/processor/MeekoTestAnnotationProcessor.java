package org.meeko.sit.processor;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import org.meeko.sit.DevLog;
import org.meeko.sit.annotation.MeekoTest;

/**
 * MeekoTest java.util.concurrent.Callable search processor
 * @author Alej4ndro G0m3z.
 *
 */
@SupportedAnnotationTypes({ "org.meeko.sit.annotation.MeekoTest", "org.meeko.sit.annotation.MeekoTestFlow" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MeekoTestAnnotationProcessor extends AbstractProcessor {

    private static final String CALLABLE_INTERFACE = "java.util.concurrent.Callable<java.lang.Object>";

    @SuppressWarnings("unused")
    private Filer               filer;
    private Messager            messager;

    @Override
    public void init(ProcessingEnvironment env) {
        filer = env.getFiler();
        messager = env.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {

//        for (TypeElement ann : annotations) {
//            if ("org.meeko.sit.annotation.MeekoTest".equals(ann.toString())) {
//                Set<? extends Element> e2s = env.getElementsAnnotatedWith(ann);
//                for (Element e2 : e2s) {
//
//                    if (e2.getKind() != ElementKind.CLASS) {
//                        messager.printMessage(Kind.ERROR, "@MeekoTest using for class only ", e2);
//                    } else {
//                        TypeElement typeElement = (TypeElement) e2;
//                        String interfaceFound = null;
//                        boolean found = false;
//                        List<? extends TypeMirror> theInterfaces = typeElement.getInterfaces();
//
//                        DevLog.log("type : " + typeElement.getSuperclass());
//
//                        /*for (int i = 0; i < theInterfaces.size() && !found; i++) {
//                            if (!theInterfaces.get(i).toString().equals(CALLABLE_INTERFACE)) {
//                                interfaceFound = theInterfaces.get(i).toString();
//                            } else {
//                                interfaceFound = null;
//                                found = true;
//                            }
//                        }*/
//
//                        if (interfaceFound != null || theInterfaces.size() == 0) {
//                            if (interfaceFound == null) {
//                                interfaceFound = "none";
//                            }
//                            messager.printMessage(Kind.ERROR, "Class using @MeekoTest must have implemented a " + CALLABLE_INTERFACE + " interface, found: " + interfaceFound, e2);
//                        }
//                    }
//                }
//            } else if ("org.meeko.sit.annotation.MeekoTestFlow".equals(ann.toString())) {
//                // Check MeekoTestFlow annotation is mandatory to use MeekoTest because we need an standard output
//                Set<? extends Element> e2s = env.getElementsAnnotatedWith(ann);
//                for (Element e2 : e2s) {
//                    if (e2.getKind() != ElementKind.FIELD) {
//                        messager.printMessage(Kind.ERROR, "@MeekoTestFlow using for field only ", e2);
//                    } else {
//                        // First check if we have the parent MeekoTest, is mandatory
//                        MeekoTest meekoTest = e2.getEnclosingElement().getAnnotation(MeekoTest.class);
//                        if (meekoTest == null) {
//                            messager.printMessage(Kind.ERROR, "@MeekoTest is mandatory with @MeekoTestFlow", e2);
//                        }
//
//                        if (!"org.meeko.sit.workflow.MeekoTestFlowService".equals(e2.asType().toString())) {
//                            messager.printMessage(Kind.ERROR, "MeekoTestFlowService class is mandatory with @MeekoTestFlow", e2);
//                        }
//                    }
//                }
//            }
//        }

        return true;
    }
}