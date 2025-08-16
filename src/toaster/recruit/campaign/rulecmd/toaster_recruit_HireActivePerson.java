package toaster.recruit.campaign.rulecmd;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;


public class toaster_recruit_HireActivePerson extends BaseCommandPlugin {
    private TextPanelAPI textPanel;

    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        Logger log = Global.getLogger(toaster_recruit_HireActivePerson.class);

        // Is the current entity a person?
        SectorEntityToken entity = dialog.getInteractionTarget();
        PersonAPI person = entity.getActivePerson();
        if (person == null) return false;

        // Check if the arguments are valid
        boolean is_admin;
        if (!params.isEmpty()) {
            is_admin = params.get(0).getBoolean(memoryMap);
        } else {
            is_admin = false;
        }
        if (params.size() > 1) {
            Misc.setMercenary(person, params.get(1).getBoolean(memoryMap));
        }

        // Log what we're trying
        log.info(String.format("Recruiting active person, admin flag: %b", is_admin));

        // Is this person based at a market? If so, remove them
        MarketAPI market = person.getMarket();
        if (market != null) {
            person.getMarket().getCommDirectory().removePerson(person);
            person.getMarket().removePerson(person);
        }

        // Add to the player's team
        if (is_admin) {
            Global.getSector().getCharacterData().addAdmin(person);
        } else {
            CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
            playerFleet.getFleetData().addOfficer(person);
        }
        return true;
    }
}
