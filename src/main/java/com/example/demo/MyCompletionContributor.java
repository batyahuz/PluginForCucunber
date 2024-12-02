//package com.example.demo;
//
//import com.intellij.codeInsight.completion.*;
//import com.intellij.codeInsight.lookup.LookupElementBuilder;
//import com.intellij.lang.Language;
//import com.intellij.openapi.application.ApplicationManager;
//import com.intellij.patterns.PlatformPatterns;
//import com.intellij.util.ProcessingContext;
//import org.jetbrains.annotations.NotNull;
//
//public class MyCompletionContributor extends CompletionContributor {
//
//    public MyCompletionContributor() {
//        Language gherkinLanguage = Language.findLanguageByID("Gherkin");
//        System.out.println("gherkinLanguage-- " + gherkinLanguage);
//        if (gherkinLanguage != null) {
//            extend(CompletionType.BASIC,
//                    PlatformPatterns.psiElement().withLanguage(gherkinLanguage),
//                    new CompletionProvider<>() {
//                        @Override
//                        protected void addCompletions(@NotNull CompletionParameters parameters,
//                                                      @NotNull ProcessingContext context,
//                                                      @NotNull CompletionResultSet result) {
////                            String[] names = {"Alice", "Bob", "Charlie"};
////                            for (String name : names) {
////                                result.addElement(LookupElementBuilder.create(name).withTypeText("name"));
////                            }
//                            System.out.println("ID-- " + parameters.getPosition().getContainingFile().getLanguage().getID());
//                            String text = parameters.getPosition().getText();
//
////                            // Check for placeholders like {page} or {name}
////                            if (text.contains("{page}")) {
////                                // Provide PAGE options
////                                result.addElement(LookupElementBuilder.create("דף הבית"));
////                                result.addElement(LookupElementBuilder.create("לקוח"));
////                                result.addElement(LookupElementBuilder.create("טופס הרשמה"));
////                            } else if (text.contains("{name}")) {
////                                // Provide NAME options
////                                result.addElement(LookupElementBuilder.create("Alice"));
////                                result.addElement(LookupElementBuilder.create("Bob"));
////                                result.addElement(LookupElementBuilder.create("Charlie"));
////                            }
//
//                            // Ensure the context is safe
//                            ApplicationManager.getApplication().invokeLater(() -> {
//                                if (text.contains("{name}")) {
//                                    result.addElement(LookupElementBuilder.create("Alice"));
//                                    result.addElement(LookupElementBuilder.create("Bob"));
//                                    result.addElement(LookupElementBuilder.create("Charlie"));
//                                } else if (text.contains("{page}")) {
//                                    result.addElement(LookupElementBuilder.create("Home"));
//                                    result.addElement(LookupElementBuilder.create("Customer"));
//                                    result.addElement(LookupElementBuilder.create("Form"));
//                                }
//                            });
//                        }
//                    });
//        }
//    }
//}
//
//package com.example.demo;
//
//import com.intellij.codeInsight.completion.*;
//import com.intellij.codeInsight.lookup.LookupElementBuilder;
//import com.intellij.patterns.PlatformPatterns;
//import com.intellij.psi.PsiElement;
//import com.intellij.util.ProcessingContext;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.*;
//
//public class MyCompletionContributor extends CompletionContributor {
//
//    // מפה שמכילה את הקטגוריות והערכים האפשריים עבור כל קטגוריה
//    private static final Map<String, List<String>> CATEGORIES = new HashMap<>();
//
//    static {
//        CATEGORIES.put("page", List.of("לקוח", "דף הבית", "טופס הרשמה"));
//        CATEGORIES.put("name", List.of("בתיה", "בוב", "ברוך", "בנימין"));
//    }
//
//    public MyCompletionContributor() {
//        // מגדיר השלמה עבור כל קטגוריה
//        for (String category : CATEGORIES.keySet()) {
//            extend(CompletionType.BASIC,
//                    PlatformPatterns.psiElement().withText("{" + category + "}"),
//                    new CompletionProvider<>() {
//                        public void addCompletions(@NotNull CompletionParameters parameters,
//                                                   @NotNull ProcessingContext context,
//                                                   @NotNull CompletionResultSet result) {
//                            List<String> values = CATEGORIES.get(category);
//                            // מסנן את הערכים לפי האותיות שהוקלדו
//                            String prefix = parameters.getOffsetContext().getStart(false).getText();
//                            values.stream()
//                                    .filter(value -> value.startsWith(prefix))
//                                    .forEach(value -> result.addElement(LookupElementBuilder.create(value)));
//                        }
//                    });
//        }
//    }
//}

//
//package com.example.demo;
//
//import com.intellij.codeInsight.completion.*;
//import com.intellij.codeInsight.lookup.LookupElementBuilder;
//import com.intellij.patterns.PlatformPatterns;
//import com.intellij.psi.PsiElement;
//import com.intellij.util.ProcessingContext;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.*;
//
//public class MyCompletionContributor extends CompletionContributor {
//
//    // מפה שמכילה את הקטגוריות והערכים האפשריים עבור כל קטגוריה
//    private static final Map<String, List<String>> CATEGORIES = new HashMap<>();
//
//    static {
//        CATEGORIES.put("page", List.of("לקוח", "דף הבית", "טופס הרשמה"));
//        CATEGORIES.put("name", List.of("Charlie", "Bob", "Alice"));
//    }
//
//    public MyCompletionContributor() {
//        // מגדיר השלמה עבור כל קטגוריה
//        for (String category : CATEGORIES.keySet()) {
//            extend(CompletionType.BASIC,
//                    PlatformPatterns.psiElement().withText("{" + category + "}"), // שימו לב כאן
//                    new CompletionProvider<CompletionParameters>() {
//                        @Override
//                        public void addCompletions(@NotNull CompletionParameters parameters,
//                                                   @NotNull ProcessingContext context,
//                                                   @NotNull CompletionResultSet result) {
//                            List<String> values = CATEGORIES.get(category);
//                            // קבלת הטקסט שהוקלד עד כה
//                            String prefix = parameters.getEditor().getCaretModel().getCurrentCaret().getSelectedText();
//
//                            if (prefix == null) {
//                                prefix = ""; // אם לא הוקלד כלום, המשך עם מיתוג ריק
//                            }
//
//                            // מסנן את הערכים לפי האותיות שהוקלדו
//                            String finalPrefix = prefix;
//                            values.stream()
//                                    .filter(value -> value.startsWith(finalPrefix))
//                                    .forEach(value -> result.addElement(LookupElementBuilder.create(value)));
//                        }
//                    });
//        }
//    }
//}
