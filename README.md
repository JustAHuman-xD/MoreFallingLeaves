# MoreFallingLeaves
MoreFallingLeaves has one purpose: to add more falling leaves to your game.
You may have noticed when you're running through a pale garden or a cherry grove that you don't really see many leaves unless you stand still, this mod aims to fix that!

Using modmenu and cloth config (or by editing `morefallingleaves.json`), you can configure the `guaranteedLeafDistance`.
- Falling leaves are spawned at randomly picked blocks in a radius around the player, blocks closer to the player are more likely to be picked.
- When a block is picked there is a specific chance for a leaf to spawn, `guaranteedLeafDistance` is a threshold for the block's distance from the player which will guarantee a spawn.
- Additionally, the closer the distance is to the `guaranteedLeafDistance`, the higher the chance for a leaf to spawn.
- Setting `guaranteedLeafDistance` to `0` will disable this mod.
- Defaults to `16` and must be within `0` and `32`.