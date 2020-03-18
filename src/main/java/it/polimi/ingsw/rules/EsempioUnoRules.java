package it.polimi.ingsw.rules;
import it.polimi.ingsw.enumerations.Tower;
import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.model.Tile;

public class EsempioUnoRules extends GodRules {

    @Override
    public void consentBuild(Tile tile) throws CantBuildException {
        if(Tower.toNumericalValue(tile.getHeight()) >= Tower.toNumericalValue(Tower.MIDDLE) || !askEnemyConsent()) {
            throw new CantBuildException("Regole 1 non possono superare MIDDLE");
        }
    }
}
