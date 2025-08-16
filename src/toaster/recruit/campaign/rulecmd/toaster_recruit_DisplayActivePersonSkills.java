package toaster.recruit.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI.SkillLevelAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc.Token;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;


public class toaster_recruit_DisplayActivePersonSkills extends BaseCommandPlugin {
    private TextPanelAPI textPanel;

    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Token> params, Map<String, MemoryAPI> memoryMap) {
        Logger log = Global.getLogger(toaster_recruit_DisplayActivePersonSkills.class);

        // Is the current entity a person?
        SectorEntityToken entity = dialog.getInteractionTarget();
        PersonAPI person = entity.getActivePerson();
        if (person == null) return false;

        TextPanelAPI text = dialog.getTextPanel();
        text.setFontSmallInsignia();

        if (!params.isEmpty()) {
            // If there's an argument, then figure out if we're showing only combat or only admin
            boolean is_admin = params.get(0).getBoolean(memoryMap);
            text.addSkillPanel(person, is_admin);

        } else {
            // If there's no argument, then show whichever skills they have
            boolean has_admin = false;
            boolean has_officer = false;

            for (SkillLevelAPI skill_mutable : person.getStats().getSkillsCopy()) {
                if (skill_mutable.getLevel() > 0) {
                    if (skill_mutable.getSkill().isAdminSkill()) {
                        log.info("Admin skill: " + skill_mutable.getSkill().getId());
                        has_admin = true;
                    } else {
                        log.info("Combat skill: " + skill_mutable.getSkill().getId());
                        has_officer = true;
                    }
                }
            }
            if (has_admin) text.addSkillPanel(person, true);
            if (has_officer) text.addSkillPanel(person, false);
        }

        text.setFontInsignia();
        return true;
    }
}
