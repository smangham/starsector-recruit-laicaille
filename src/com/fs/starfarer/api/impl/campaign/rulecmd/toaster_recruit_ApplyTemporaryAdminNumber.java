package com.fs.starfarer.api.impl.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.util.Misc.Token;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;


public class toaster_recruit_ApplyTemporaryAdminNumber extends BaseCommandPlugin {
    private TextPanelAPI textPanel;

    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Token> params, Map<String, MemoryAPI> memoryMap) {
        Logger log = Global.getLogger(toaster_recruit_ApplyTemporaryAdminNumber.class);

        // Check if the arguments are valid
        int modifier_value;
        String modifier_id;

        if (!params.isEmpty()) {
            modifier_id = params.get(0).string;
        } else {
            log.error("No modifier ID provided");
            return false;
        }

        if (params.size() > 1) {
            modifier_value = params.get(1).getInt(memoryMap);
        } else {
            modifier_value = 1;
        }

        // Log what we're doing
        log.info(String.format("Applying modifier: %s, %d", modifier_id, modifier_value));

        Global.getSector().getPlayerStats().getAdminNumber().modifyFlat(modifier_id, modifier_value);
        return true;
    }
}
