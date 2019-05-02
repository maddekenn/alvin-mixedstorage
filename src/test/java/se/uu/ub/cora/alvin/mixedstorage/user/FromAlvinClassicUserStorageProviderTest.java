/*
 * Copyright 2019 Uppsala University Library
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
package se.uu.ub.cora.alvin.mixedstorage.user;

import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.alvin.mixedstorage.log.LoggerFactorySpy;
import se.uu.ub.cora.gatekeeper.user.UserStorageProvider;
import se.uu.ub.cora.logger.LoggerProvider;
import se.uu.ub.cora.sqldatabase.DataReader;
import se.uu.ub.cora.storage.UserStorageImp;
import se.uu.ub.cora.storage.testdata.TestDataAppTokenStorage;

public class FromAlvinClassicUserStorageProviderTest {
	private String basePath = "/tmp/recordStorageOnDiskTemp/";
	private Map<String, String> initInfo;
	private LoggerFactorySpy loggerFactorySpy;
	private String testedClassName = "FromAlvinClassicUserStorageProvider";

	@BeforeMethod
	public void makeSureBasePathExistsAndIsEmpty() throws IOException {
		loggerFactorySpy = new LoggerFactorySpy();
		LoggerProvider.setLoggerFactory(loggerFactorySpy);
		File dir = new File(basePath);
		dir.mkdir();
		deleteFiles(basePath);
		TestDataAppTokenStorage.createRecordStorageInMemoryWithTestData(basePath);

		initInfo = new HashMap<>();
		initInfo.put("storageOnDiskBasePath", basePath);
	}

	private void deleteFiles(String path) throws IOException {
		Stream<Path> list;
		list = Files.list(Paths.get(path));

		list.forEach(p -> deleteFile(p));
		list.close();
	}

	private void deleteFile(Path path) {
		try {
			if (path.toFile().isDirectory()) {
				deleteFiles(path.toString());
			}
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterMethod
	public void removeTempFiles() throws IOException {
		if (Files.exists(Paths.get(basePath))) {
			deleteFiles(basePath);
			File dir = new File(basePath);
			dir.delete();
		}
	}

	@Test
	public void testInit() throws Exception {
		UserStorageProvider userStorageProvider = new FromAlvinClassicUserStorageProvider();
		userStorageProvider.startUsingInitInfo(initInfo);
		AlvinMixedUserStorage userStorage = (AlvinMixedUserStorage) userStorageProvider
				.getUserStorage();
		UserStorageImp userStorageForGuest = (UserStorageImp) userStorage.getUserStorageForGuest();
		// todo: type userStorageForGuest
		assertSame(userStorageForGuest.getInitInfo(), initInfo);
		DataReader dataReader = userStorage.getDataReaderForUsers();
		// assertSame(userStorage.getInitInfo(), initInfo);
		// type etc. for data reader
		assertTrue(false);
	}

	@Test
	public void testOnlyOneInstanceOfUserStorageIsReturned() throws Exception {
		UserStorageProvider userStorageProvider = new FromAlvinClassicUserStorageProvider();
		userStorageProvider.startUsingInitInfo(initInfo);
		AlvinMixedUserStorage userStorage = (AlvinMixedUserStorage) userStorageProvider
				.getUserStorage();
		AlvinMixedUserStorage userStorage2 = (AlvinMixedUserStorage) userStorageProvider
				.getUserStorage();
		assertSame(userStorage, userStorage2);
	}
}