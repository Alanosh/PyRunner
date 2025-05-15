from bip_utils import Bip39MnemonicGenerator, Bip39SeedGenerator
from solana.keypair import Keypair

mnemonic = Bip39MnemonicGenerator().FromWordsNumber(12)
seed = Bip39SeedGenerator(mnemonic).Generate()
keypair = Keypair.from_seed(seed[:32])
print("Generated mnemonic:", mnemonic)
print("Derived public key:", keypair.public_key)
