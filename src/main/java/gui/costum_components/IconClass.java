package gui.costum_components;

import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.swing.FontIcon;

import java.awt.*;

public class IconClass {
   private final FontIcon searcherIcon = FontIcon.of(FontAwesome.SEARCH_PLUS, 24);
   private final FontIcon mapIcon = FontIcon.of(FontAwesome.GLOBE, 24);
   private final FontIcon homeIcon = FontIcon.of(FontAwesome.HOME,100,new Color(0x59A869));
   private final FontIcon tipoVenditaIcon = FontIcon.of(FontAwesome.BUILDING_O, 24);
   private final FontIcon numeroBagniIcon = FontIcon.of(FontAwesome.SHOWER, 24);
   private final FontIcon numeroStanzeIcon = FontIcon.of(FontAwesome.SQUARE_O, 24);
   private final FontIcon classeEnergeticaIcon = FontIcon.of(FontAwesome.BOLT, 24);
   private final FontIcon pianiIcon = FontIcon.of(FontAwesome.ARROW_UP, 24);

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
}
