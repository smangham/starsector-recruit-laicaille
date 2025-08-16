// Borrowed with permission from Jaydee Piracy
package toaster.recruit.campaign.rulecmd;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc.Token;
import com.fs.starfarer.rpg.Person;


public class toaster_recruit_DisplaySkill extends BaseCommandPlugin {
    private TextPanelAPI textPanel;

    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Token> params, Map<String, MemoryAPI> memoryMap) {
        Logger log = Global.getLogger(toaster_recruit_DisplaySkill.class);

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

        // Log what we're doing
        log.info(String.format("Displaying skill: %s", skill_id));

        SkillSpecAPI spec = Global.getSettings().getSkillSpec(skill_id);

        Person fake = (Person) Global.getFactory().createPerson();
        fake.setFaction(Factions.SLEEPER);
        fake.getStats().setSkillLevel(skill_id, skill_level);

        TextPanelAPI text = dialog.getTextPanel();
        text.setFontSmallInsignia();
        text.addSkillPanel(fake, spec.isAdminSkill());
        text.setFontInsignia();
        return true;
    }
}
