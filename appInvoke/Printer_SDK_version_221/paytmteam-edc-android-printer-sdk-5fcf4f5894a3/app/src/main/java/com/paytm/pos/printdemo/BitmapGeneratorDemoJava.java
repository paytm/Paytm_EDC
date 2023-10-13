package com.paytm.pos.printdemo;

import android.content.Context;

import androidx.core.content.res.ResourcesCompat;

import com.paytm.printgenerator.Alignment;
import com.paytm.printgenerator.FontSize;
import com.paytm.printgenerator.page.DashElement;
import com.paytm.printgenerator.page.GapElement;
import com.paytm.printgenerator.page.Page;
import com.paytm.printgenerator.page.TextElement;

public class BitmapGeneratorDemoJava {

    public static Page getDemoPage(Context context) {
        Page page = new Page();

        //Single Line
        page.addLine().addElement(new TextElement("Demo Print"));

        //Inverted Text
        page.addLine().addElement(new TextElement(true, "Demo Print"));

        //Different Text Size
        page.addLine().addElement(
                new TextElement(
                        false,
                        false,
                        FontSize.FONT_MEDIUM,
                        "Demo Print"));

        //Different Alignment and Bold
        page.addLine().addElement(
                new TextElement(
                        false,
                        true,
                        FontSize.FONT_NORMAL,
                        "Demo Print",
                        null,
                        Alignment.RIGHT));

        //Multiple elements by weight with different alignments
        page.addLine()
                .addElement(
                        new TextElement(
                                false,
                                false, FontSize.FONT_NORMAL,
                                "Text left",
                                null, Alignment.LEFT,
                                0,
                                1))
                .addElement(
                        new TextElement(
                                false,
                                false, FontSize.FONT_NORMAL,
                                "Text Right",
                                null, Alignment.RIGHT,
                                0,
                                1));

        //Multiple elements by weight
        page.addLine()
                .addElement(new TextElement(false, false, FontSize.FONT_SMALL, "Text1", null, Alignment.LEFT, 0, 1))
                .addElement(new GapElement(true, 30, 1))
                .addElement(new TextElement(false, false, FontSize.FONT_SMALL, "Text2", null, Alignment.LEFT, 0, 1))
                .addElement(new TextElement(false, false, FontSize.FONT_SMALL, "Text3", null, Alignment.LEFT, 0, 1))
                .addElement(new TextElement(false, false, FontSize.FONT_SMALL, "Text4", null, Alignment.LEFT, 0, 1))
                .addElement(new GapElement(true, 30, 1))
                .addElement(new TextElement(false, false, FontSize.FONT_SMALL, "Text5", null, Alignment.LEFT, 0, 1));

        //Special Characters parsing
        page.addLine().addElement(new TextElement(" Multi\n\t Line\n\t\t Text\n\t\t\tCascade"));

        //Text wrapping
        page.addLine().addElement(new TextElement("This is a long string for testing text wrapping and given padding. This should be center aligned and with proper padding"));

        page.addLine().addElement(
                new TextElement(
                        false,
                        false, FontSize.FONT_MEDIUM,
                        "This is a long string for testing text wrapping and given padding. This should be center aligned and with proper padding",
                        ResourcesCompat.getFont(context, R.font.orange_juice)
                        ));


        //DASH ELEMENT DEMO
        page.addLine().addElement(new DashElement(12, 3));

        return page;
    }

}
