package gui.costum_components;

import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IconClass {
   private final Color mainColor = Color.decode("#59A869");
   private final FontIcon searcherIcon = FontIcon.of(FontAwesome.SEARCH_PLUS, 24);
   private final FontIcon mapIcon = FontIcon.of(FontAwesome.GLOBE, 24);
   private final FontIcon homeIcon = FontIcon.of(FontAwesome.HOME,100,this.mainColor);
   private final FontIcon homeButtonIcon = FontIcon.of(FontAwesome.HOME, 24);
   private final FontIcon tipoVenditaIcon = FontIcon.of(FontAwesome.BUILDING_O, 24);
   private final FontIcon numeroBagniIcon = FontIcon.of(FontAwesome.SHOWER, 24);
   private final FontIcon numeroStanzeIcon = FontIcon.of(FontAwesomeSolid.COUCH, 24);
   private final FontIcon classeEnergeticaIcon = FontIcon.of(FontAwesome.BOLT, 24);
   private final FontIcon pianiIcon = FontIcon.of(FontAwesomeSolid.ARROW_UP, 24);
   private final FontIcon accountAministazioneIcon = FontIcon.of(FontAwesome.USERS, 24);
   private final FontIcon logInIcon = FontIcon.of(FontAwesomeSolid.DOOR_CLOSED,24);
   private final FontIcon logOutIcon = FontIcon.of(FontAwesomeSolid.DOOR_OPEN, 24);
   private final FontIcon ascensoreIcon = FontIcon.of(FontAwesome.SORT, 22);
   private final FontIcon balconeIcon = FontIcon.of(FontAwesome.COLUMNS, 22);
   private final FontIcon mapCenterMarker = FontIcon.of(FontAwesomeSolid.MAP_MARKER_ALT,20,this.mainColor);
   private final FontIcon cursoreMapSelect = FontIcon.of(FontAwesomeSolid.MAP_MARKER,28,this.mainColor);
   private final FontIcon calendarioIcon = FontIcon.of(FontAwesome.CALENDAR_O,30);
   private final FontIcon cantinaIcon = FontIcon.of(FontAwesomeSolid.DUNGEON,22);
   private final FontIcon climatizzazioneIcon = FontIcon.of(FontAwesomeSolid.FIRE_ALT,22);
   private final FontIcon portineriaIcon = FontIcon.of(FontAwesomeSolid.USER_LOCK,22);
   private final FontIcon mapImmobiliIcon = FontIcon.of(FontAwesomeSolid.MAP_MARKER_ALT,24,Color.green);


    public Color getMainColor() {return mainColor;}

    public MouseAdapter cursorButton(){
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                // Ripristina il cursore quando il mouse esce dal bottone
                e.getComponent().setCursor(Cursor.getDefaultCursor());
            }
        };
    }


    public FontIcon getSearcherIcon() {
        return searcherIcon;
    }
    public FontIcon getMapIcon() {
        return mapIcon;
    }
    public FontIcon getHomeIcon() {
        return homeIcon;
    }
    public FontIcon getTipoVenditaIcon() {
        return tipoVenditaIcon;
    }
    public FontIcon getNumeroBagniIcon() {
        return numeroBagniIcon;
    }
    public FontIcon getNumeroStanzeIcon() {
        return numeroStanzeIcon;
    }
    public FontIcon getClasseEnergeticaIcon() {
        return classeEnergeticaIcon;
    }
    public FontIcon getPianiIcon() {
        return pianiIcon;
    }
    public FontIcon getAccountAministazioneIcon() {
        return accountAministazioneIcon;
    }
    public FontIcon getLogInIcon() {
        return logInIcon;
    }
    public FontIcon getLogOutIcon() {
        return logOutIcon;
    }
    public FontIcon getHomeButtonIcon() {
        return homeButtonIcon;
    }
    public FontIcon getMapCenterMarker(){return mapCenterMarker;}
    public FontIcon getAscensoreIcon() {return ascensoreIcon;}
    public FontIcon getBalconeIcon() {return balconeIcon;}
    public FontIcon getCursoreMapSelect() {return cursoreMapSelect;}
    public FontIcon getCalendarioIcon() {return calendarioIcon;}
    public FontIcon getPortineriaIcon() {return portineriaIcon;}
    public FontIcon getClimatizzazioneIcon() {return climatizzazioneIcon;}
    public FontIcon getCantinaIcon() {return cantinaIcon;}
    public FontIcon getMapImmobiliIcon() {return mapImmobiliIcon;}

}
