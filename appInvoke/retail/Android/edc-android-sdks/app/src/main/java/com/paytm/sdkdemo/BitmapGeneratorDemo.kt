package com.paytm.pos.printdemo

import android.graphics.Bitmap
import com.paytm.printgenerator.*
import com.paytm.printgenerator.page.*
import com.paytm.printgenerator.printer.Printer

class BitmapGeneratorDemo {

    /**
     * This is a demo class for BitmapGenerator class.
     * It can be used as a reference, though not exhaustive, for designing a Bitmap.
     *
     * It also Demos how a Bitmap can be generated without the Generator Runnable.
     *
     * @see BitmapGeneratorRunnable
     * @see Page
     * @see BitmapGenerator
     *
     * @author Sagar Sharma
     * @contributors
     */

    companion object {

        /**
         * This Demo function generated Bitmap for the Demo Page object on the same thread.
         */
        fun generateDemoSync(bitmap : Bitmap): Bitmap {
            return Printer.generateBitmapSync(getDemoPage(bitmap))
        }

        /**
         * This Demo function generates Bitmap for the Demo Page object on a background thread.
         */
        fun generateDemoAsync(bitmap: Bitmap, callback : BitmapReceiverCallback) {
            Printer.generateBitmapAsync(getDemoPage(bitmap), callback)
        }

        /**
         * This Demo function generates a Page object with varying combinations to showcase possibilities.
         */
        fun getDemoPage( bitmap : Bitmap) : Page {
            val page = Page()

            //TEXT ELEMENT DEMO

            //Single Line
            page.addLine().addElement(TextElement(text = "Demo Print"))

            //Inverted Text
            page.addLine().addElement(TextElement(text = "Demo Print", dispInvert = true))

            //Different Text Size
            page.addLine().addElement(TextElement(text = "Demo Print", textSize = FontSize.FONT_MEDIUM))

            //Different Alignment and Bold
            page.addLine().addElement(TextElement(text = "Demo Print", alignment = Alignment.RIGHT, bold = true))

            //Multiple elements by weight with different alignments
            page.addLine()
                .addElement(TextElement(text = "Text Left", widthWeight = 1, alignment = Alignment.LEFT))
                .addElement(TextElement(text = "Text Right", widthWeight = 1, alignment = Alignment.RIGHT))

            //Multiple elements by weight
            page.addLine()
                .addElement(TextElement(text = "Text1", widthWeight = 1))
                .addElement(GapElement(width = 30, widthWeight = 1, dispInvert = true))
                .addElement(TextElement(text = "Text2", widthWeight = 1))
                .addElement(TextElement(text = "Text3", widthWeight = 1))
                .addElement(TextElement(text = "Text4", widthWeight = 1))
                .addElement(GapElement(width = 30, widthWeight = 1, dispInvert = true))
                .addElement(TextElement(text = "Text5", widthWeight = 1))

            //Special Characters parsing
            page.addLine().addElement(TextElement(text = " Multi\n\t Line\n\t\t Text\n\t\t\tCascade"))

            //Text wrapping
            page.addLine().addElement(TextElement(text = "This is a long string for testing text wrapping and given padding. This should be left aligned and with proper padding", paddingLeft = 30, paddingRight = 30, alignment = Alignment.LEFT))

            //Height and Width restrictions
            page.addLine().addElement(TextElement(text = "Height and width restricted Text", textSize = FontSize.FONT_MEDIUM, height = 15, width = 30))

            //Width override by Weight
            page.addLine().addElement(TextElement(text = "Weight override Width", widthWeight = 1, width = 300)).addElement(TextElement(text = "Override"))

            //Simple Bitmap
            page.addLine().addElement(ImageElement(image = bitmap))

            //Multiple Images with Alignment, width by weight and scaling
            page.addLine()
                .addElement(ImageElement(image = bitmap, widthWeight = 1, shrinkToFit = true, alignment = Alignment.LEFT))
                .addElement(ImageElement(image = bitmap, widthWeight = 1, shrinkToFit = true, alignment = Alignment.RIGHT))

            // Image with Background, height restriction and scaling
            page.addLine().addElement(ImageElement(image = bitmap, height = 30, shrinkToFit = true, backgroundColor = PageColor.WHITE))

            //Image rotation
            page.addLine()
                .addElement(ImageElement(image = bitmap, widthWeight = 1, shrinkToFit = true))
                .addElement(ImageElement(image = bitmap, widthWeight = 1, shrinkToFit = true, rotate = Rotation.ROTATE_90))
                .addElement(ImageElement(image = bitmap, widthWeight = 1, shrinkToFit = true, rotate = Rotation.ROTATE_180))
                .addElement(ImageElement(image = bitmap, widthWeight = 1, shrinkToFit = true, rotate = Rotation.ROTATE_270))


            //DASH ELEMENT DEMO
            page.addLine().addElement(DashElement(dashHeight = 3, height = 12))

            return page
        }

    }
}