package eu.simplecompliance.ublvalidator.schematron;

import java.util.List;

public record SchematronResult(boolean ok, List<String> messages) {}
