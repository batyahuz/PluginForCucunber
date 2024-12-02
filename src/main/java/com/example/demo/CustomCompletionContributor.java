//package com.example.demo;
//
//import com.intellij.codeInsight.lookup.LookupArranger;
//import com.intellij.codeInsight.lookup.LookupElement;
//import com.intellij.codeInsight.lookup.LookupElementBuilder;
//import com.intellij.codeInsight.lookup.LookupManager;
//import com.intellij.openapi.editor.Editor;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.ui.Messages;
//import com.intellij.psi.PsiElement;
//
//import java.util.List;
//
//public class CustomCompletionContributor {
//
//    public void triggerCompletion(Project project, Editor editor, PsiElement element) {
//        // הדפסת הודעה לקונסול כדי לוודא שהפלאגין עובד
//        System.out.println("CustomCompletionContributor triggered!");
//
//        // יצירת אובייקטים מסוג LookupElement להשלמת הטקסט
//        LookupElement pageElement = LookupElementBuilder.create("PAGE");
//        LookupElement nameElement = LookupElementBuilder.create("NAME");
//
//        LookupElement[] elements = new LookupElement[]{pageElement, nameElement};
//
//        // הצגת האפשרויות להשלמת טקסט
//        LookupManager lookupManager = LookupManager.getInstance(project);
//
//        // יצירת LookupArranger עם ערכים ברירת מחדל
//        LookupArranger arranger = LookupArranger.DEFAULT;
//
//        // הצגת ההשלמה עם 4 פרמטרים: Editor, LookupElement[], String, LookupArranger
//        lookupManager.createLookup(editor, elements, " ", arranger)
//        System.out.println("Page and Name completions added!");
//    }
//
//    public void triggerPopup(Project project, PsiElement element) {
//        // הצגת הודעה בחלון
//        Messages.showMessageDialog(
//                project,
//                "פלאגין עובד!",
//                "בדיקת פלאגין",
//                Messages.getInformationIcon()
//        );
//    }
//}
