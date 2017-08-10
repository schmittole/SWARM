class StagedText {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    String finalText;
    int stepCounter = 0;
    int interval = 1;
    int counter = 0;



    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    StagedText() {}
    StagedText(String v) { finalText = v; }
    StagedText(int v) { interval = v; }



    /*---------------------------------------------------------------
    Functions
    ----------------------------------------------------------------*/

    void setInterval(int v) {
        interval = v;
    }

    void setText(String s) {
        finalText = s;
        stepCounter = min(stepCounter, finalText.length());
    }

    String getCurrentText() {
        if (counter%interval == 1) {
            if (stepCounter < finalText.length()) { stepCounter++; }
        }
        counter++;
        return finalText.substring(0, stepCounter);
    }

}
