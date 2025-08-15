package toaster.recruit.skills;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import org.apache.log4j.Logger;

public class IndependentMinded {
    private static final Logger log = Logger.getLogger(IndependentMinded.class);
    public static int ADMINS = 1;
    public static int STABILITY_BONUS = 1;
    public static float INCOME_MULT_PENALTY = 0.2f;
    public static String TEMPORARY_MODIFIER_ID = "toaster_recruit_independent_minded_admin_number";

    public static class Level1 implements MarketSkillEffect {
        public void apply(MarketAPI market, String id, float level) {
			market.getIncomeMult().modifyMult(id, 1 - INCOME_MULT_PENALTY, "Independent Minded");
		}

		public void unapply(MarketAPI market, String id) {
			market.getUpkeepMult().unmodify(id);
		}

		public String getEffectDescription(float level) {
			return "-" + (int)Math.round(Math.abs(INCOME_MULT_PENALTY * 100f)) + "% income penalty";
		}

		public String getEffectPerLevelDescription() {
			return null;
		}

		public ScopeDescription getScopeDescription() {
			return ScopeDescription.GOVERNED_OUTPOST;
		}
    }

    public static class Level2 implements MarketSkillEffect {
        public void apply(MarketAPI market, String id, float level) {
            market.getStability().modifyFlat(id, STABILITY_BONUS, "Independent Minded");
        }

        public void unapply(MarketAPI market, String id) {
            market.getStability().unmodifyMult(id);
        }

        public String getEffectDescription(float level) {
            return "+" + STABILITY_BONUS + " stability for governed colony";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    public static class Level3 implements MarketSkillEffect {
        public void apply(MarketAPI market, String id, float level) {
           log.info(String.format("Applying with ID: %s", id));
           Global.getSector().getPlayerStats().getAdminNumber().modifyFlat(id, ADMINS);
           Global.getSector().getPlayerStats().getAdminNumber().unmodify(TEMPORARY_MODIFIER_ID);
        }

        public void unapply(MarketAPI market, String id) {
            Global.getSector().getPlayerStats().getAdminNumber().unmodify(id);
        }

        public String getEffectDescription(float level) {
            return "Increase administrator limit by 1 whilst assigned";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }
}
