package gui.costum_components;

import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IconClass {

    // 🔹 Colori principali
    private static final Color MAIN_COLOR = Color.decode("#59A869");

    // 🔹 Icone principali
    private static final FontIcon SEARCHER_ICON = FontIcon.of(FontAwesome.SEARCH_PLUS, 24);
    private static final FontIcon MAP_ICON = FontIcon.of(FontAwesome.GLOBE, 24);
    private static final FontIcon HOME_ICON = FontIcon.of(FontAwesome.HOME, 100, MAIN_COLOR);
    private static final FontIcon HOME_BUTTON_ICON = FontIcon.of(FontAwesome.HOME, 24);

    // 🔹 Icone proprietà immobiliari
    private static final FontIcon TIPO_VENDITA_ICON = FontIcon.of(FontAwesome.BUILDING_O, 24);
    private static final FontIcon NUMERO_BAGNI_ICON = FontIcon.of(FontAwesome.SHOWER, 24);
    private static final FontIcon NUMERO_STANZE_ICON = FontIcon.of(FontAwesomeSolid.COUCH, 24);
    private static final FontIcon CLASSE_ENERGETICA_ICON = FontIcon.of(FontAwesome.BOLT, 24);
    private static final FontIcon PIANI_ICON = FontIcon.of(FontAwesomeSolid.ARROW_UP, 24);

    // 🔹 Icone utenti e sessione
    private static final FontIcon ACCOUNT_AMINISTAZIONE_ICON = FontIcon.of(FontAwesome.USERS, 24);
    private static final FontIcon LOGIN_ICON = FontIcon.of(FontAwesomeSolid.DOOR_CLOSED, 24);
    private static final FontIcon LOGOUT_ICON = FontIcon.of(FontAwesomeSolid.DOOR_OPEN, 24);

    // 🔹 Icone funzionali
    private static final FontIcon ASCENSORE_ICON = FontIcon.of(FontAwesome.SORT, 22);
    private static final FontIcon BALCONE_ICON = FontIcon.of(FontAwesome.COLUMNS, 22);
    private static final FontIcon MAP_CENTER_MARKER = FontIcon.of(FontAwesomeSolid.MAP_MARKER_ALT, 20, MAIN_COLOR);
    private static final FontIcon CURSORE_MAP_SELECT = FontIcon.of(FontAwesomeSolid.MAP_MARKER, 28, MAIN_COLOR);
    private static final FontIcon CALENDARIO_ICON = FontIcon.of(FontAwesome.CALENDAR_O, 30);
    private static final FontIcon CANTINA_ICON = FontIcon.of(FontAwesomeSolid.DUNGEON, 22);
    private static final FontIcon CLIMATIZZAZIONE_ICON = FontIcon.of(FontAwesomeSolid.FIRE_ALT, 22);
    private static final FontIcon PORTINERIA_ICON = FontIcon.of(FontAwesomeSolid.USER_LOCK, 22);
    private static final FontIcon MAP_IMMOBILI_ICON = FontIcon.of(FontAwesomeSolid.MAP_MARKER_ALT, 24, Color.green);
    private static final FontIcon PLUS_IMMOBILE = FontIcon.of(FontAwesomeSolid.PLUS_CIRCLE, 24);
    private static final FontIcon PASSWORD_ICON = FontIcon.of(FontAwesomeSolid.LOCK, 24);
    private static final FontIcon EMAIL_ICON = FontIcon.of(FontAwesomeSolid.ENVELOPE, 24);
    private static final FontIcon NOME_CLIENTE = FontIcon.of(FontAwesomeSolid.USER, 24);
    private static final FontIcon BACK_BUTTON = FontIcon.of(FontAwesomeSolid.ARROW_ALT_CIRCLE_LEFT, 30);

    private static final FontIcon SCUOLA_ICON = FontIcon.of(FontAwesomeSolid.SCHOOL, 18);
    private static final FontIcon TRANSPORT_ICON = FontIcon.of(FontAwesomeSolid.TRAIN, 18);
    private static final FontIcon PARCHI_ICON = FontIcon.of(FontAwesomeSolid.TREE, 18);


    //Font
    private static final Font FONT_IMMOBILE_CARATTERISTICHE = new Font("SansSerif", Font.PLAIN, 18);


    // 🔹 Metodo per il cursore a mano sui bottoni
    public static MouseAdapter cursorButton() {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setCursor(Cursor.getDefaultCursor());
            }
        };
    }

    // 🔹 Getter
    public static Color getMainColor() { return MAIN_COLOR; }
    public static FontIcon getSearcherIcon() { return SEARCHER_ICON; }
    public static FontIcon getMapIcon() { return MAP_ICON; }
    public static FontIcon getHomeIcon() { return HOME_ICON; }
    public static FontIcon getHomeButtonIcon() { return HOME_BUTTON_ICON; }
    public static FontIcon getTipoVenditaIcon() { return TIPO_VENDITA_ICON; }
    public static FontIcon getNumeroBagniIcon() { return NUMERO_BAGNI_ICON; }
    public static FontIcon getNumeroStanzeIcon() { return NUMERO_STANZE_ICON; }
    public static FontIcon getClasseEnergeticaIcon() { return CLASSE_ENERGETICA_ICON; }
    public static FontIcon getPianiIcon() { return PIANI_ICON; }
    public static FontIcon getAccountAministazioneIcon() { return ACCOUNT_AMINISTAZIONE_ICON; }
    public static FontIcon getLogInIcon() { return LOGIN_ICON; }
    public static FontIcon getLogOutIcon() { return LOGOUT_ICON; }
    public static FontIcon getAscensoreIcon() { return ASCENSORE_ICON; }
    public static FontIcon getBalconeIcon() { return BALCONE_ICON; }
    public static FontIcon getMapCenterMarker() { return MAP_CENTER_MARKER; }
    public static FontIcon getCursoreMapSelect() { return CURSORE_MAP_SELECT; }
    public static FontIcon getCalendarioIcon() { return CALENDARIO_ICON; }
    public static FontIcon getCantinaIcon() { return CANTINA_ICON; }
    public static FontIcon getClimatizzazioneIcon() { return CLIMATIZZAZIONE_ICON; }
    public static FontIcon getPortineriaIcon() { return PORTINERIA_ICON; }
    public static FontIcon getMapImmobiliIcon() { return MAP_IMMOBILI_ICON; }
    public static FontIcon getPlusImmobile() { return PLUS_IMMOBILE; }
    public static FontIcon getPasswordIcon() { return PASSWORD_ICON; }
    public static FontIcon getEmailIcon() { return EMAIL_ICON; }
    public static FontIcon getNomeCliente() { return NOME_CLIENTE; }
    public static Font getFontImmobiliC() { return FONT_IMMOBILE_CARATTERISTICHE; }
    public static FontIcon getBackButton(){return  BACK_BUTTON;}
    public static FontIcon getScuolaIcon() { return SCUOLA_ICON; }
    public static FontIcon getTransportIcon() { return TRANSPORT_ICON; }
   public static  FontIcon getParchiIcon() { return PARCHI_ICON; }
}
