class ModeController {


    /*---------------------------------------------------------------
    Fields
    ----------------------------------------------------------------*/

    Mode activeMode;
    int currentAppMode = 0;
    //String currentGameMode = "A";
    int currentGameMode = 0;
    //HashMap<String, GameMode> gameModes = new HashMap<String, GameMode>();
    HashMap<String, Mode> appModes = new HashMap<String, Mode>();
    ArrayList<GameMode> gameModes = new ArrayList<GameMode>();

    /*---------------------------------------------------------------
    Constructor
    ----------------------------------------------------------------*/

    ModeController() {
        //activeMode = "introMode";
    }


    void addGameMode(GameMode g) {
        gameModes.add(g);
    }

    /*void addGameMode(String s, GameMode g) {
        gameModes.put(s, g);
    }*/

    void addAppMode(String s, Mode g) {
        appModes.put(s, g);
    }

    /*GameMode getGameMode(String s) {
        return gameModes.get(s);
    }*/

    GameMode getGameMode(int i) {
        return gameModes.get(i);
    }

    Mode getAppMode(String s) {
        return appModes.get(s);
    }

    Mode getActiveAppMode() {
        return activeMode;
    }

    void setActiveAppMode(String s) {
        activeMode = getAppMode(s);
    }

    /*void setCurrentGameMode(String s) {
        currentGameMode = s;
        changeAppMode(currentAppMode);
    }*/

    void setCurrentGameMode(int i) {
        currentGameMode = i;
        changeAppMode(currentAppMode);
    }

    void nextAppMode() {
        if (currentAppMode < appModes.size()) { currentAppMode++; }
        changeAppMode(currentAppMode);
    }

    void previousAppMode() {
        if (currentAppMode > 0) { currentAppMode--; }
        changeAppMode(currentAppMode);
    }


    void nextGameMode() {
        if (currentGameMode < gameModes.size()-1) { currentGameMode++; }
        changeAppMode(currentAppMode);
    }

    void previousGameMode() {
        if (currentGameMode > 0) { currentGameMode--; }
        changeAppMode(currentAppMode);
    }


    void changeAppMode(int newMode) {

        if      (newMode == 0) { activeMode = getAppMode("introMode"); }
        else if (newMode == 1) { activeMode = getAppMode("trackingMode"); }
        else if (newMode == 2) { activeMode = getGameMode(currentGameMode); }
        else if (newMode == 3) { activeMode = getAppMode("finalMode"); }

        // intro function of activeMode
        activeMode.intro();

    }

}
