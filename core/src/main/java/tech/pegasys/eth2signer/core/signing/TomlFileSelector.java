/*
 * Copyright 2020 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.eth2signer.core.signing;

import tech.pegasys.signers.secp256k1.api.FileSelector;
import tech.pegasys.signers.secp256k1.api.PublicKey;

import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;

public class TomlFileSelector implements FileSelector<PublicKey> {

  private static final String TOML_FILE_EXT = "toml";

  @Override
  public Filter<Path> getAllConfigFilesFilter() {
    return this::matchesFileExtension;
  }

  @Override
  public Filter<Path> getSpecificConfigFileFilter(PublicKey publicKey) {
    return (entry) -> {
      final String baseName = FilenameUtils.getBaseName(entry.toString());
      final String identifier = normaliseIdentifier(publicKey.toString().toLowerCase());
      return matchesFileExtension(entry) && baseName.toLowerCase().endsWith(identifier);
    };
  }

  private boolean matchesFileExtension(final Path entry) {
    return FilenameUtils.getExtension(entry.toString()).equalsIgnoreCase(TOML_FILE_EXT);
  }

  private String normaliseIdentifier(final String signerIdentifier) {
    return signerIdentifier.toLowerCase().startsWith("0x")
        ? signerIdentifier.substring(2)
        : signerIdentifier;
  }
}