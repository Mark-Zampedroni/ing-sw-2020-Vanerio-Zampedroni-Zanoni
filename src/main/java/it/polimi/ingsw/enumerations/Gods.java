package it.polimi.ingsw.enumerations;

import it.polimi.ingsw.rules.*;
import it.polimi.ingsw.rules.GodSharedRules;

public enum Gods {
    APOLLO, ARTEMIS, ATHENA, ATLAS, DEMETER, HEPHAESTUS, MINOTAUR, PAN, PROMETHEUS, ZEUS, TRITON, POSEIDON, HESTIA;

    public static GodSharedRules create(Gods god) {
        GodSharedRules rule;
        switch(god) {
            case APOLLO: rule = new ApolloRules();
            case PAN: rule = new PanRules();
            case ATLAS: rule = new AtlasRules();
            case ATHENA: rule = new AthenaRules();
            case ARTEMIS: rule = new ArtemisRules();
            case DEMETER: rule = new DemeterRules();
            case MINOTAUR: rule = new MinotaurRules();
            case HEPHAESTUS: rule = new HephaestusRules();
            case PROMETHEUS: rule = new PrometheusRules();
            case ZEUS: rule = new ZeusRules();
            case TRITON: rule = new TritonRules();
            case POSEIDON: rule = new PoseidonRules();
            case HESTIA: rule = new HestiaRules();
            default: rule = new GodRules(); // Can't occur
        }
        return rule;
    }


}
