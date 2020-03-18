package it.polimi.ingsw.rules;
import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.model.Tile;

public abstract class GodRules {

    public void consentBuild(Tile tile) throws CantBuildException {
        if(tile.getCompleted() || !askEnemyConsent()) {
            throw new CantBuildException("La costruzione e' già completa");
        }
    }

    public boolean askEnemyConsent() { return true; } // Qui si chiede consenso alle regole dei nemici
}
