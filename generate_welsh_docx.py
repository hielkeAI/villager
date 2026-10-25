import zipfile, os
from xml.sax.saxutils import escape

doc = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
<w:body>
"""

def para(text="", bold=False, size=11, italic=False, style=None, space_after=8):
    global doc
    ppr = ""
    if style: ppr = "<w:pStyle w:val=\"%s\"/>" % style
    if space_after is not None: ppr += "<w:spacing w:after=\"%d\"/>" % (space_after*20)
    runpr = ""
    if bold: runpr += "<w:b/>"
    if italic: runpr += "<w:i/>"
    runpr += "<w:sz w:val=\"%d\"/>" % (size*2)
    doc += "<w:p><w:pPr>%s</w:pPr><w:r><w:rPr>%s</w:rPr><w:t xml:space=\"preserve\">%s</w:t></w:r></w:p>" % (ppr, runpr, escape(text))

def heading(text, size=16):
    para(text, bold=True, size=size, space_after=6)

def bullet(text):
    para("• " + text, size=11, space_after=4)

# Title
para("Siopau Ffyddlon a Chistiau — Canllaw Hawdd", bold=True, size=20, space_after=4)
para("DpShop — add-on Minecraft ar gyfer ffrindiau'ch gweinydd (Cymru yn gyntaf!)", italic=True, size=11, space_after=12)

heading("1. Beth yw hwn?")
para("Mae'n ategyn/datapack sy'n troi unrhyw pentrefwr a unrhyw gist yn siop. Mae prisau'n cychwyn ar 0% treth, gall chwaraewyr greu siopau eu hunain, a'r iaith ddiofyn yw Cymraeg.")

heading("2. Sut i'w osod (hawdd iawn!)")
para("Cam 1 — Datapack:")
bullet("Copïwch ffolder 'datapack' i within-dyn/datapacks/.")
bullet("Teipiwch /reload yn y gêm.")
bullet("Dylai neges aur ymddangos: [DPShops] wedi lwytho…")
para("Cam 2 — Ategyn (dewisol, ar gyfer ffrindiau):")
bullet("Copïwch DpShop.jar i plugins/. (Gofynnwch i'r IT os oes angen.)")
bullet("Ailgychwyn y gweinydd.")

heading("3. Sut i wneud siop (hylaw!)")
para("Siop pentrefwr:")
bullet("Ail-enwwch tag enw i '[Shop]' yn yr adl (anvil) a dde-gliciwch ar bennaeth pentrefwr.")
bullet("Bydd chwaraewyr sy'n de-glicio'r pentrefwr yn agor y siop.")
para("Siop gist (ChestShop, arddull hawdd):")
bullet("Gosodwch gist. Rhowch eitemau yn y gist.")
bullet("Gosodwch arwydd ar y gist.")
bullet("Ysgrifennwch 'shop' ar linell 1 (neu 'admin' ar gyfer stoc anfeidrol).")
bullet("Mae'r arwydd yn llenwi'n awtomatig (perchennog, swm, pris P/G, eitem).")
bullet("De-glic = prynu, chwith-glic = gwerthu, shift+de-glic = agor y gist.")

heading("4. Ieithoedd")
para("Iaith ddiofyn: Cymraeg (lang: cy).")
bullet("English: lang: en")
bullet("Yorkshire (tafodiaith): lang: yks")
para("Gosodwch 'lang' yn plugins/DpShop/config.yml.")

heading("5. Treth")
para("Treth ddiofyn: 0%. Gweinyddwyr: newidiwch 'shop.tax-percent' yn config (0–100).")

heading("6. Gorchmynion (Cymru + English)")
bullet("/cartref — cartref (home).")
bullet("/gosodcartref — gosod cartref (set home).")
bullet("/dileucartref — dileu (delete home).")
bullet("/cartrefau — rhestr cartrefi.")
bullet("/rtp — teleport hap (random).")
bullet("/galw — galw chwaraewr (tpa).")
bullet("/galwyfan — galw fan hyn (tpahere).")
bullet("/derbyn — derbyn (accept).")
bullet("/gwrthod — gwrthod (deny).")
bullet("/pay — talu (pay).")
bullet("/dpshop vbuy <rhif> / vsell <rhif> — prynu/gwerthu o siop pentrefwr.")
bullet("/dpshop vrestock — ail-stocio (dim ond gweinydd).")
bullet("/dpshop grant — rhoi arian (admin).")

heading("7. Siopau pentrefwyr (ffurfweddadwy)")
para("Gall CHWARAEWYR ffurfweddu unrhyw bennaeth:")
bullet("De-glic ar bennaeth → siop yn agor.")
bullet("/dpshop vclaim — hawlio'r pentrefwr (ei siop yn wag).")
bullet("/dpshop vadd <eitem> <prynu> <gwerthu> <max> — ychwanegu eitem.")
bullet("/dpshop vset <rhif> <eitem> <prynu> <gwerthu> <max> — gosod eitem.")
bullet("/dpshop vremove <rhif> — dileu eitem.")
para("Mae'r stoc yn ail-stocio'n ddyddiol. Mae adnabyddiaeth (reputation) yn gostwng prisiau (hyd at 50%!).")

heading("8. Yammer? / Cwestiynau")
para("Os yw rhywbeth ddim yn gweithio, gofynnwch i'ch IT neu admin gweinydd. Mwynhewch eich siopau! 💛")
"""

body = ""
# Build paragraphs by converting simple markdown-ish text built above? Instead replicate: We'll parse lines.
# We generated plain text above as 'doc'? Actually we built 'doc' xml via functions? No, we didn't call them. Let's just build body from text content manually using function calls by re-executing with structured content.

# Rebuild using functions
doc = "<w:body>"
def H(t):
    global doc
    doc += "<w:p><w:pPr><w:pStyle w:val=\"Heading1\"/><w:spacing w:after=\"120\"/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val=\"32\"/></w:rPr><w:t xml:space=\"preserve\">%s</w:t></w:r></w:p>" % escape(t)
def P(t, bold=False, italic=False, size=11):
    global doc
    rp = ""
    if bold: rp += "<w:b/>"
    if italic: rp += "<w:i/>"
    rp += "<w:sz w:val=\"%d\"/>" % (size*2)
    doc += "<w:p><w:pPr><w:spacing w:after=\"160\"/></w:pPr><w:r><w:rPr>%s</w:rPr><w:t xml:space=\"preserve\">%s</w:t></w:r></w:p>" % (rp, escape(t))
def B(t):
    global doc
    doc += "<w:p><w:pPr><w:spacing w:after=\"80\"/></w:pPr><w:r><w:t xml:space=\"preserve\">• %s</w:t></w:r></w:p>" % escape(t)

P("Siopau Ffyddlon a Chistiau — Canllaw Hawdd", bold=True, size=20)
P("DpShop — add-on Minecraft ar gyfer ffrindiau'ch gweinydd (Cymru yn gyntaf!)", italic=True, size=11)
H("1. Beth yw hwn?")
P("Mae'n ategyn/datapack sy'n troi unrhyw bennafwr a unrhyw gist yn siop. Mae prisau'n cychwyn ar 0% treth, gall chwaraewyr greu siopau eu hunain, a'r iaith ddiofyn yw Cymraeg.")
H("2. Sut i'w osod (hawdd iawn!)")
P("Cam 1 — Datapack:")
B("Copïwch ffolder 'datapack' i within-dyn/datapacks/.")
B("Teipiwch /reload yn y gêm.")
B("Dylai neges aur ymddangos: [DPShops] wedi llytho…")
P("Cam 2 — Ategyn (dewisol, ar gyfer ffrindiau):")
B("Copïwch DpShop.jar i plugins/.")
B("Ailgychwyn y gweinydd.")
H("3. Sut i wneud siop (hylaw!)")
P("Siop pentrefwr:")
B("Ail-enwwch tag enw i '[Shop]' yn yr adl (anvil) a de-gliciwch ar bennaeth pentrefwr.")
B("Bydd chwaraewyr sy'n de-glicio'r pentrefwr yn agor y siop.")
P("Siop gist (ChestShop, arddull hawdd):")
B("Gosodwch gist. Rhowch eitemau yn y gist.")
B("Gosodwch arwydd ar y gist.")
B("Ysgrifennwch 'shop' ar linell 1 (neu 'admin' ar gyfer stoc anfeidrol).")
B("Mae'r arwydd yn llenwi'n awtomatig (perchennog, swm, pris P/G, eitem).")
B("De-glic = prynu, chwith-glic = gwerthu, shift+de-glic = agor y gist.")
H("4. Ieithoedd")
P("Iaith ddiofyn: Cymraeg (lang: cy).")
B("English: lang: en")
B("Yorkshire (tafodiaith): lang: yks")
P("Gosodwch 'lang' yn plugins/DpShop/config.yml.")
H("5. Treth")
P("Treth ddiofyn: 0%. Gweinyddwyr: newidiwch 'shop.tax-percent' yn config (0–100).")
H("6. Gorchmynion (Cymru + English)")
B("/cartref — cartref (home).")
B("/gosodcartref — gosod cartref (set home).")
B("/dileucartref — dileu (delete home).")
B("/cartrefau — rhestr cartrefi.")
B("/rtp — teleport hap (random).")
B("/galw — galw chwaraewr (tpa).")
B("/galwyfan — galw fan hyn (tpahere).")
B("/derbyn — derbyn (accept).")
B("/gwrthod — gwrthod (deny).")
B("/pay — talu (pay).")
B("/dpshop vbuy <rhif> / vsell <rhif> — prynu/gwerthu o siop pentrefwr.")
B("/dpshop vrestock — ail-stocio (admin).")
B("/dpshop grant — rhoi arian (admin).")
B("/dpshop vclaim, vadd, vset, vremove — ffurfweddu siopau.")
H("7. Siopau pentrefwyr (ffurfweddadwy gan chwaraewyr)")
P("Gall CHWARAEWYR ffurfweddu unrhyw bennaeth:")
B("/dpshop vclaim — hawlio'r pentrefwr (ei siop yn wag).")
B("/dpshop vadd <eitem> <prynu> <gwerthu> <max> — ychwanegu eitem.")
B("/dpshop vset <rhif> <eitem> <prynu> <gwerthu> <max> — gosod eitem.")
B("/dpshop vremove <rhif> — dileu eitem.")
P("Mae'r stoc yn ail-stocio'n ddyddiol. Mae adnabyddiaeth (reputation) yn gostwng prisiau (hyd at 50%!).")
H("8. Cwestiynau?")
P("Os oes problem, gofynnwch i'ch admin gweinydd. Mwynhewch eich siopau! 💛")
doc += "</w:body></w:document>"

content_types = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
<Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
<Default Extension="xml" ContentType="application/xml"/>
<Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
</Types>"""

rels = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
</Relationships>"""

out = "/home/test124/Documents/datapack/Siopau_DpShop_Cymraeg.docx"
with zipfile.ZipFile(out, "w") as z:
    z.writestr("[Content_Types].xml", content_types)
    z.writestr("_rels/.rels", rels)
    z.writestr("word/document.xml", doc)
print("created", out)
PY
python3 /home/test124/Documents/datapack/generate_welsh_docx.py