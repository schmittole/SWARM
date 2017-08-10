class FontFamily {

    HashMap<String, String> fontFiles = new HashMap<String, String>();
    HashMap<String, PFont> generatedFonts = new HashMap<String, PFont>();

    FontFamily() {}

    void addFile(String weight, String style, String file) {
        fontFiles.put(weight+"_"+style, file);
    }

    String getFile(String weight, String style) {
        return fontFiles.get(weight+"_"+style);
    }

    PFont getFont(String weight, String style, int size) {

        PFont font = generatedFonts.get(weight+"_"+style+"_"+size);

        if (font == null) {
            generatedFonts.put(weight+"_"+style+"_"+size, createFont(this.getFile(weight, style), size));
            font = generatedFonts.get(weight+"_"+style+"_"+size);
        }

        return font;
    }

}
