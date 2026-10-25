import zipfile
from xml.sax.saxutils import escape

H1 = lambda t: "<w:p><w:pPr><w:pStyle w:val='Heading1'/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val='32'/></w:rPr><w:t xml:space='preserve'>%s</w:t></w:r></w:p>" % escape(t)
P = lambda t, bold=False, italic=False, size=11: "<w:p><w:r><w:rPr>%s<w:sz w:val='%d'/></w:rPr><w:t xml:space='preserve'>%s</w:t></w:r></w:p>" % (("","<w:b/>")[bold]+("","<w:i/>")[italic], size*2, escape(t))
BUL = lambda t: "<w:p><w:r><w:t xml:space='preserve'>• %s</w:t></w:r></w:p>" % escape(t)

body = ""
body += P("Siopau Ffyddlon a Chistiau — Canllaw Hawdd", bold=True, size=20)
body += P("DpShop — add-on Minecraft ar gyfer ffrindiau'ch gweinydd (Cymru yn gyntaf!)", italic=True, size=11)
body += H1("1. Beth yw hwn?")
body += P("Mae'n ategyn/datapack sy'n troi unrhyw bennaeth a unrhyw gist yn siop. 0% treth yn ddiofyn, gall chwaraewyr greu siopau eu hunain, a Chymraeg yw'r iaith ddiofyn.")
body += H1("2. Sut i'w osod (hawdd iawn!)")
body += P("Cam 1 — Datapack:")
body += BUL("Copïwch ffolder 'datapack' i within-dyn/datapacks/.")
body += BUL("Teipiwch /reload yn y gêm.")
body += BUL("Dylai neges aur ymddangos: [DPShops] wedi llwytho…")
body += P("Cam 2 — Ategyn (dewisol):")
body += BUL("Copïwch DpShop.jar i plugins/.")
body += BUL("Ailgychwyn y gweinydd.")
body += H1("3. Sut i wneud siop")
body += P("Siop pentrefwr:")
body += BUL("Ail-enwwch tag enw i '[Shop]' yn yr adl (anvil) a de-gliciwch ar bennaeth.")
body += BUL("De-glicio'r pentrefwr = agor y siop.")
body += P("Siop gist (ChestShop, hawdd):")
body += BUL("Gosodwch gist a rhowch eitemau ynddi.")
body += BUL("Gosodwch arwydd ar y gist.")
body += BUL("Ysgrifennwch 'shop' ar linell 1 (neu 'admin' ar gyfer stoc anfeidrol).")
body += BUL("Mae'r arwydd yn llenwi'n awtomatig (perchennog, swm, pris P/G, eitem).")
body += BUL("De-glic = prynu, chwith-glic = gwerthu, shift+de-glic = agor y gist.")
body += H1("4. Ieithoedd")
body += P("Iaith ddiofyn: Cymraeg (lang: cy).")
body += BUL("English: lang: en")
body += BUL("Yorkshire (tafodiaith): lang: yks")
body += P("Gosodwch 'lang' yn plugins/DpShop/config.yml.")
body += H1("5. Treth")
body += P("Treth ddiofyn: 0%. Gweinyddwyr: newidiwch 'shop.tax-percent' yn config (0–100).")
body += H1("6. Gorchmynion (Cymru + English)")
for t in ["/cartref — cartref (home).","/gosodcartref — set home.","/dileucartref — delete home.","/cartrefau — list homes.","/rtp — random teleport.","/galw — tpa (request).","/galwyfan — tpahere.","/derbyn — accept.","/gwrthod — deny.","/pay — pay a player.","/dpshop vbuy/vsell <n> — villager shop.","/dpshop vclaim,vadd,vset,vremove — configure villagers.","/dpshop vrestock, grant — admin."]:
    body += BUL(t)
body += H1("7. Siopau pentrefwyr (ffurfweddadwy gan chwaraewyr)")
body += BUL("/dpshop vclaim — hawlio pennafwr (siop wag).")
body += BUL("/dpshop vadd <eitem> <prynu> <gwerthu> <max> — ychwanegu.")
body += BUL("/dpshop vset <n> <eitem> <prynu> <gwerthu> <max> — gosod.")
body += BUL("/dpshop vremove <n> — dileu.")
body += P("Mae stoc yn ail-stocio'n ddyddiol. Mae adnabyddiaeth (reputation) yn gostwng prisiau (hyd at 50%!).")
body += H1("8. Cwestiynau?")
body += P("Os oes problem, gofynnwch i'ch admin gweinydd. Mwynhewch eich siopau! 💛")

docxml = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"><w:body>' + body + '</w:body></w:document>'
ct = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types"><Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/><Default Extension="xml" ContentType="application/xml"/><Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/></Types>'
rels = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships"><Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/></Relationships>'

out = "/home/test124/Documents/datapack/Siopau_DpShop_Cymraeg.docx"
with zipfile.ZipFile(out, "w") as z:
    z.writestr("[Content_Types].xml", ct)
    z.writestr("_rels/.rels", rels)
    z.writestr("word/document.xml", docxml)
print("created", out)