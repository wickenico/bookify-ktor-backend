.PHONY:dependency-report
dependency-report:
	./gradlew dependencyUpdates

.PHONY:dependency-update
dependency-update:
	./gradlew versionCatalogUpdate
