package buildings;

import java.awt.Color;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;

public class administrativecomplex extends BaseIndustry {
	public int unavailablereason = 0;
	public float UPKEEP_MULT= 1.5f;

	public void apply() {
		super.apply(true);
		Global.getSector().getPlayerPerson().getStats().getAdminNumber().modifyFlat("adminincrease",1);
		market.getStability().modifyFlat("adminstab",3,"administrative nexus");
		int size = market.getSize();
		if(size<5)
		{
			demand(Commodities.DOMESTIC_GOODS, 3);
		}
		else
		{
			demand(Commodities.DOMESTIC_GOODS,size - 1);
		}
		if(isDisrupted())
		{
			market.getStability().modifyFlat("disruption", -5,"administrative nexus disruption");
		}
		else
		{
			market.getStability().unmodifyFlat("disruption");
		}
		if(Global.getSettings().getModManager().isModEnabled("aotd_vok"))
		{
			demand("advanced_components", market.getSize()-3);
		}

	}
	public void unapply(){
		super.unapply();
		market.getStability().unmodifyFlat("adminstab");
		Global.getSector().getPlayerPerson().getStats().getAdminNumber().unmodifyFlat("adminincrease");
	}
	@Override
	protected void applyAlphaCoreModifiers() {
		Global.getSector().getPlayerPerson().getStats().getAdminNumber().modifyFlat("alphaadminincease",1);
		getUpkeep().modifyMult("alphaupkeepincrease",2,"alpha Core assigned");
	}
	@Override
	protected void applyNoAICoreModifiers(){
		Global.getSector().getPlayerPerson().getStats().getAdminNumber().unmodifyFlat("alphaadminincease");
		getUpkeep().unmodifyMult("alphaupkeepincrease");
	}
	protected void addAlphaCoreDescription(TooltipMakerAPI tooltip, AICoreDescriptionMode mode) {
		float opad = 10f;
		Color highlight = Misc.getHighlightColor();

		String pre = "Alpha-level AI core currently assigned. ";
		if (mode == AICoreDescriptionMode.MANAGE_CORE_DIALOG_LIST || mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
			pre = "Alpha-level AI core. ";
		}
		float a = 1;
		String str = "" + (int) Math.round(a) ;

		if (mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
			CommoditySpecAPI coreSpec = Global.getSettings().getCommoditySpec(aiCoreId);
			TooltipMakerAPI text = tooltip.beginImageWithText(coreSpec.getIconName(), 48);
			text.addPara(pre + "increases upkeep cost by %s. Reduces demand by %s unit. " +
							"Increases administrator limit by %s.", 0f, highlight,
					"" + (int)((1f - UPKEEP_MULT) * 100f) + "%", "" + DEMAND_REDUCTION,
					str);
			tooltip.addImageWithText(opad);
			return;
		}

		tooltip.addPara(pre + "increases upkeep cost by %s. Reduces demand by %s unit. " +
						"Increases administrator limit by %s.", opad, highlight,
				"" + (int)((1f - UPKEEP_MULT) * 100f) + "%", "" + DEMAND_REDUCTION,
				str);

	}
	@Override
	public boolean isAvailableToBuild()
	{
		if(market.getSize() < 6 )
		{
			unavailablereason = 0;
			return false;
		}
		return true;


	}
	@Override
	public String getUnavailableReason() {
		if (!super.isAvailableToBuild()) return super.getUnavailableReason();
		return "cannot be built on planets smaller than size 6";
	}
	@Override
	protected void addRightAfterDescriptionSection(TooltipMakerAPI tooltip, IndustryTooltipMode mode)
	{
		super.addRightAfterDescriptionSection(tooltip, mode);
		tooltip.addPara("increases administrator capacity by 1 \nincrease stability by 3", 10f);
	}

}









