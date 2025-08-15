package com.fs.starfarer.api.impl.campaign.rulecmd;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.util.Misc;


/**
 * Gets the current active person in dialogue, and adds a skill to them at rank 1 or higher.
 *
 * Parameters are:
 * - string: The skill ID.
 * - integer, optional: The level to add. Defaults to 1, i.e. not Elite.
 */
public class toaster_recruit_AddSkillToActivePerson extends BaseCommandPlugin {
    private TextPanelAPI textPanel;

    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        Logger log = Global.getLogger(toaster_recruit_AddSkillToActivePerson.class);

        // Is the current entity a person?
        SectorEntityToken entity = dialog.getInteractionTarget();
        PersonAPI person = entity.getActivePerson();
        if (person == null) return false;

        // Check if the arguments are valid
        int skill_level;
        String skill_id;

        if (!params.isEmpty()) {
            skill_id = params.get(0).string;
        } else {
            log.error("No Skill ID provided");
            return false;
        }

        if (params.size() > 1) {
            skill_level = params.get(1).getInt(memoryMap);
        } else {
            skill_level = 1;
        }

        // Log what we're doing, and add the skill
        log.info(String.format("Adding skill to active person - %s, %d", skill_id, skill_level));
        person.getStats().setSkillLevel(skill_id, skill_level);
        return true;
    }
}
