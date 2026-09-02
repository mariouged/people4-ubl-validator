package eu.simplecompliance.ublvalidator.schematron;

import java.util.List;

public record XmlSchemaResult(boolean ok, List<String> messages) {}
