package it.unipd.dei.esp1617.excuseznous.excuseznousservice;

 class Constants {
    public interface ACTION {
        //azioni assocciate alla pressione dei bottoni che gestiscono le azioni del service
          String MAIN_ACTION = "button.start";
          String PAUSE_ACTION = "button.pause";
          String PLAY_ACTION = "button.play";
          String CANCEL_ACTION = "button.cancel";
    }

    public interface STATUS {
        //stato in cui si trova il service
          String START = "start";
          String PLAY = "play";
          String REPLAY = "replay";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}
