# comp2021-oj


## Docker Start
```bash
docker compose up -d
```

Enter the container
```bash
docker compose exec dev bash
```

Verify the installation
```bash
java --version
git --version
```

## Compile

```bash
python /oj/compile.py
```


## Run
```bash
cd /cut
java -cp out hk.edu.polyu.comp.comp2021.cvfs.Application
```
