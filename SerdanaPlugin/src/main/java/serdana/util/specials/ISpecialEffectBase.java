package main.java.serdana.util.specials;

import main.java.serdana.util.infos.SpecialPlayerInfo;

public interface ISpecialEffectBase {
	public void start(SpecialPlayerInfo info);
	public void tick(SpecialPlayerInfo info);
}
