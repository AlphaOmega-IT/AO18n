package de.alphaomegait.ao18n;

import me.blvckbytes.bukkitevaluable.IConfigPathsProvider;

public interface IAO18nProvider extends IConfigPathsProvider {

	void initialize(
		final boolean forceReplacement
	);

	@Override
	String[] getConfigPaths();
}
