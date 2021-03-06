/*
 * Copyright 2015 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.storage.testdata;

import se.uu.ub.cora.basicstorage.RecordStorageInMemory;
import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataPart;
import se.uu.ub.cora.data.converter.JsonToDataConverter;
import se.uu.ub.cora.data.converter.JsonToDataConverterFactory;
import se.uu.ub.cora.data.converter.JsonToDataConverterFactoryImp;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;
import se.uu.ub.cora.storage.RecordStorage;

public class TestDataRecordInMemoryStorage {
	private static DataGroup emptyCollectedData = DataCreator.createEmptyCollectedData();

	public static RecordStorageInMemory createRecordStorageInMemoryWithTestData() {
		RecordStorageInMemory recordsInMemory = new RecordStorageInMemory();
		addPlace(recordsInMemory);
		addSecondPlace(recordsInMemory);
		addMetadata(recordsInMemory);
		addPresentation(recordsInMemory);
		addText(recordsInMemory);
		addRecordType(recordsInMemory);
		addRecordTypeRecordType(recordsInMemory);
		addRecordTypeBinary(recordsInMemory);
		addRecordTypeImage(recordsInMemory);
		addRecordTypeGenericBinary(recordsInMemory);
		addRecordTypeRecordTypeAutoGeneratedId(recordsInMemory);
		addRecordTypePlace(recordsInMemory);
		addRecordTypeAbstractAuthority(recordsInMemory);
		addChildRecordTypeOfAbstractAuthority(recordsInMemory);
		addChildToChildToRecordTypeOfAbstractAuthority(recordsInMemory);
		addRecordTypeSearchTerm(recordsInMemory);
		addSearchTerm(recordsInMemory);
		addSomeSearchTerm(recordsInMemory);
		addMetadataRecordTypes(recordsInMemory);
		addRecordTypeGenericCollectionItem(recordsInMemory);
		addGenericCollectionItem(recordsInMemory);

		addRecordTypeCollectTerm(recordsInMemory);
		addCollectIndexTerm(recordsInMemory);
		addRecordTypeCollectIndexTerm(recordsInMemory);
		addRecordTypeCollectPermissionTerm(recordsInMemory);

		DataGroup dummy = DataGroup.withNameInData("dummy");
		recordsInMemory.create("metadataCollectionVariable", "dummy1", dummy, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
		recordsInMemory.create("metadataCollectionVariableChild", "dummy1", dummy,
				emptyCollectedData, DataGroup.withNameInData("dummy"), "cora");
		recordsInMemory.create("metadataItemCollection", "dummy1", dummy, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
		recordsInMemory.create("metadataCollectionItem", "dummy1", dummy, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
		recordsInMemory.create("metadataTextVariable", "dummy1", dummy, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
		recordsInMemory.create("metadataNumberVariable", "dummy1", dummy, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
		recordsInMemory.create("metadataRecordLink", "dummy1", dummy, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
		recordsInMemory.create("metadataRecordRelation", "dummyRecordRelation", dummy,
				emptyCollectedData, DataGroup.withNameInData("collectedLinksList"), "cora");
		recordsInMemory.create("metadataResourceLink", "dummyResourceLink", dummy,
				emptyCollectedData, DataGroup.withNameInData("collectedLinksList"), "cora");
		return recordsInMemory;
	}

	private static void addPlace(RecordStorageInMemory recordsInMemory) {
		DataGroup recordInfo = DataCreator.createRecordInfoWithRecordTypeAndRecordId("place",
				"place:0001");
		DataGroup dataGroup = DataGroup.withNameInData("authority");
		dataGroup.addChild(recordInfo);

		recordsInMemory.create("place", "place:0001", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addSecondPlace(RecordStorage recordsInMemory) {
		DataGroup recordInfo = DataCreator.createRecordInfoWithRecordTypeAndRecordId("place",
				"place:0002");

		DataGroup dataGroup = DataGroup.withNameInData("authority");
		dataGroup.addChild(recordInfo);

		DataGroup dataRecordLink = DataGroup.withNameInData("link");
		dataGroup.addChild(dataRecordLink);
		addLinkedRecordTypeAndLinkedRecordIdToRecordLink("place", "place:0001", dataRecordLink);

		// dataGroup.addChild(DataRecordLink.withNameInDataAndLinkedRecordTypeAndLinkedRecordId("link",
		// "place", "place:0001"));

		DataGroup collectedLinksList = createLinkList();
		recordsInMemory.create("place", "place:0002", dataGroup,
				DataCreator.createEmptyCollectedData(), collectedLinksList, "cora");
	}

	private static void addLinkedRecordTypeAndLinkedRecordIdToRecordLink(
			String linkedRecordTypeString, String linkedRecordIdString, DataGroup dataRecordLink) {
		DataAtomic linkedRecordType = DataAtomic.withNameInDataAndValue("linkedRecordType",
				linkedRecordTypeString);
		dataRecordLink.addChild(linkedRecordType);

		DataAtomic linkedRecordId = DataAtomic.withNameInDataAndValue("linkedRecordId",
				linkedRecordIdString);
		dataRecordLink.addChild(linkedRecordId);
	}

	private static DataGroup createLinkList() {
		DataGroup collectedLinksList = DataGroup.withNameInData("collectedLinksList");
		DataGroup recordToRecordLink = DataGroup.withNameInData("recordToRecordLink");

		DataGroup from = DataGroup.withNameInData("from");
		recordToRecordLink.addChild(from);
		addLinkedRecordTypeAndLinkedRecordIdToRecordLink("place", "place:0002", from);
		// DataRecordLink from = DataRecordLink
		// .withNameInDataAndLinkedRecordTypeAndLinkedRecordId("from", "place",
		// "place:0002");
		DataGroup to = DataGroup.withNameInData("to");
		recordToRecordLink.addChild(to);
		addLinkedRecordTypeAndLinkedRecordIdToRecordLink("place", "place:0001", to);

		// DataRecordLink to =
		// DataRecordLink.withNameInDataAndLinkedRecordTypeAndLinkedRecordId("to",
		// "place", "place:0001");
		// recordToRecordLink.addChild(to);

		collectedLinksList.addChild(recordToRecordLink);
		return collectedLinksList;
	}

	private static void addMetadata(RecordStorageInMemory recordsInMemory) {
		String metadata = "metadataGroup";
		DataGroup dataGroup = DataGroup.withNameInData("metadata");

		DataGroup recordInfo = DataCreator.createRecordInfoWithRecordTypeAndRecordId(metadata,
				"place");
		dataGroup.addChild(recordInfo);
		recordsInMemory.create(metadata, "place", dataGroup, DataCreator.createEmptyCollectedData(),
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addPresentation(RecordStorageInMemory recordsInMemory) {
		String presentation = "presentation";
		DataGroup dataGroup = DataGroup.withNameInData(presentation);

		DataGroup recordInfo = DataCreator.createRecordInfoWithRecordTypeAndRecordId(presentation,
				"placeView");
		dataGroup.addChild(recordInfo);

		recordsInMemory.create(presentation, "placeView", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addText(RecordStorageInMemory recordsInMemory) {
		String text = "text";
		DataGroup dataGroup = DataGroup.withNameInData("text");

		DataGroup recordInfo = DataCreator.createRecordInfoWithRecordTypeAndRecordId(text,
				"placeText");
		dataGroup.addChild(recordInfo);
		recordsInMemory.create(text, "placeText", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addRecordType(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";
		DataGroup dataGroup = DataGroup.withNameInData(recordType);

		DataGroup recordInfo = DataCreator.createRecordInfoWithRecordTypeAndRecordId(recordType,
				"metadata");
		dataGroup.addChild(recordInfo);

		dataGroup.addChild(DataAtomic.withNameInDataAndValue("abstract", "false"));
		recordsInMemory.create(recordType, "metadata", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addRecordTypeRecordType(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";
		DataGroup dataGroup = DataCreator
				.createRecordTypeWithIdAndUserSuppliedIdAndAbstract("recordType", "true", "false");
		recordsInMemory.create(recordType, "recordType", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addRecordTypeRecordTypeAutoGeneratedId(
			RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";
		DataGroup dataGroup = DataCreator.createRecordTypeWithIdAndUserSuppliedIdAndAbstract(
				"recordTypeAutoGeneratedId", "false", "false");
		recordsInMemory.create(recordType, "recordTypeAutoGeneratedId", dataGroup,
				emptyCollectedData, DataGroup.withNameInData("collectedLinksList"), "cora");

	}

	private static void addRecordTypeBinary(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";
		DataGroup dataGroup = DataCreator
				.createRecordTypeWithIdAndUserSuppliedIdAndAbstract("binary", "true", "true");
		recordsInMemory.create(recordType, "binary", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addRecordTypeImage(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";
		DataGroup dataGroup = DataCreator
				.createRecordTypeWithIdAndUserSuppliedIdAndParentId("image", "true", "binary");
		recordsInMemory.create(recordType, "image", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addRecordTypeGenericBinary(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";
		DataGroup dataGroup = DataCreator.createRecordTypeWithIdAndUserSuppliedIdAndParentId(
				"genericBinary", "true", "binary");
		recordsInMemory.create(recordType, "genericBinary", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addRecordTypeGenericCollectionItem(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";
		DataGroup dataGroup = DataCreator.createRecordTypeWithIdAndUserSuppliedIdAndParentId(
				"genericCollectionItem", "true", "metadataCollectionItem");
		recordsInMemory.create(recordType, "genericCollectionItem", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addGenericCollectionItem(RecordStorageInMemory recordsInMemory) {
		String recordType = "genericCollectionItem";
		DataGroup dataGroup = DataGroup.withNameInData("genericCollectionItem");

		DataGroup recordInfo = DataCreator.createRecordInfoWithRecordTypeAndRecordId(recordType,
				"someItem");
		dataGroup.addChild(recordInfo);
		recordsInMemory.create(recordType, "someItem", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addRecordTypePlace(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";

		DataGroup dataGroup = DataCreator
				.createRecordTypeWithIdAndUserSuppliedIdAndAbstract("place", "false", "false");

		recordsInMemory.create(recordType, "place", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addRecordTypeAbstractAuthority(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";

		DataGroup dataGroup = DataCreator.createRecordTypeWithIdAndUserSuppliedIdAndAbstract(
				"abstractAuthority", "false", "true");

		recordsInMemory.create(recordType, "abstractAuthority", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addChildRecordTypeOfAbstractAuthority(
			RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";

		DataGroup dataGroup = DataCreator.createRecordTypeWithIdAndUserSuppliedIdAndParentId(
				"childToAbstractAuthority", "false", "abstractAuthority");

		recordsInMemory.create(recordType, "childToAbstractAuthority", dataGroup,
				emptyCollectedData, DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addChildToChildToRecordTypeOfAbstractAuthority(
			RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";

		DataGroup dataGroup = DataCreator.createRecordTypeWithIdAndUserSuppliedIdAndParentId(
				"grandChildToAbstractAuthority", "false", "childToAbstractAuthority");

		recordsInMemory.create(recordType, "grandChildToAbstractAuthority", dataGroup,
				emptyCollectedData, DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addRecordTypeSearchTerm(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";

		DataGroup dataGroup = DataCreator
				.createRecordTypeWithIdAndUserSuppliedIdAndAbstract("searchTerm", "false", "false");

		recordsInMemory.create(recordType, "searchTerm", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addSearchTerm(RecordStorageInMemory recordsInMemory) {
		DataGroup dataGroup = DataGroup.withNameInData("searchTerm");

		DataGroup recordInfo = DataCreator.createRecordInfoWithRecordTypeAndRecordId("searchTerm",
				"titleSearchTerm");
		dataGroup.addChild(recordInfo);

		recordsInMemory.create("searchTerm", "titleSearchTerm", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addSomeSearchTerm(RecordStorageInMemory recordsInMemory) {
		String searchTermJson = "{\"name\":\"searchTerm\",\"children\":[{\"name\":\"recordInfo\",\"children\":[{\"name\":\"id\",\"value\":\"someSearchTerm\"},{\"name\":\"type\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"searchTerm\"}]},{\"name\":\"createdBy\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"141414\"}]},{\"name\":\"dataDivider\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}]}]},{\"name\":\"searchTermType\",\"value\":\"linkedData\"},{\"name\":\"searchFieldRef\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"metadata\"},{\"name\":\"linkedRecordId\",\"value\":\"refTextVar\"}]},{\"name\":\"indexType\",\"value\":\"indexTypeString\"}]}";
		DataGroup searchTerm = convertJsonStringToDataGroup(searchTermJson);
		recordsInMemory.create("searchTerm", "someSearchTerm", searchTerm, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "systemOne");
	}

	private static DataGroup convertJsonStringToDataGroup(String jsonRecord) {
		JsonParser jsonParser = new OrgJsonParser();
		JsonValue jsonValue = jsonParser.parseString(jsonRecord);
		JsonToDataConverterFactory jsonToDataConverterFactory = new JsonToDataConverterFactoryImp();
		JsonToDataConverter jsonToDataConverter = jsonToDataConverterFactory
				.createForJsonObject(jsonValue);
		DataPart dataPart = jsonToDataConverter.toInstance();
		return (DataGroup) dataPart;
	}

	private static void addRecordTypeCollectTerm(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";
		DataGroup dataGroup = DataCreator
				.createRecordTypeWithIdAndUserSuppliedIdAndAbstract("collectTerm", "true", "true");
		recordsInMemory.create(recordType, "collectTerm", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addRecordTypeCollectIndexTerm(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";
		DataGroup dataGroup = DataCreator.createRecordTypeWithIdAndUserSuppliedIdAndParentId(
				"collectIndexTermId", "true", "collectTerm");
		recordsInMemory.create(recordType, "collectIndexTerm", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addCollectIndexTerm(RecordStorageInMemory recordsInMemory) {
		DataGroup dataGroup = DataGroup.withNameInData("collectTerm");

		DataGroup recordInfo = DataCreator.createRecordInfoWithRecordTypeAndRecordId(
				"collectIndexTerm", "collectIndexTermId");
		dataGroup.addChild(recordInfo);

		recordsInMemory.create("collectIndexTerm", "collectIndexTermId", dataGroup,
				emptyCollectedData, DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addRecordTypeCollectPermissionTerm(RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";
		DataGroup dataGroup = DataCreator.createRecordTypeWithIdAndUserSuppliedIdAndParentId(
				"collectPermissionTermId", "true", "collectTerm");
		recordsInMemory.create(recordType, "collectPermissionTerm", dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}

	private static void addMetadataRecordTypes(RecordStorageInMemory recordsInMemory) {

		createRecordTypeForWithIdAndAbstract("metadataGroup", "false", recordsInMemory);
		createRecordTypeForWithIdAndAbstract("metadataTextVariable", "false", recordsInMemory);
		createRecordTypeForWithIdAndAbstract("metadataNumberVariable", "false", recordsInMemory);
		createRecordTypeForWithIdAndAbstract("metadataCollectionVariable", "false",
				recordsInMemory);
		createRecordTypeForWithIdAndAbstract("metadataItemCollection", "false", recordsInMemory);
		createRecordTypeForWithIdAndAbstract("metadataCollectionItem", "true", recordsInMemory);
		createRecordTypeForWithIdAndAbstract("metadataRecordLink", "false", recordsInMemory);
		createRecordTypeForWithIdAndAbstract("metadataResourceLink", "false", recordsInMemory);
	}

	private static void createRecordTypeForWithIdAndAbstract(String id, String abstractValue,
			RecordStorageInMemory recordsInMemory) {
		String recordType = "recordType";
		DataGroup dataGroup = DataCreator.createRecordTypeWithIdAndUserSuppliedIdAndAbstract(id,
				"false", abstractValue);

		recordsInMemory.create(recordType, id, dataGroup, emptyCollectedData,
				DataGroup.withNameInData("collectedLinksList"), "cora");
	}
}
