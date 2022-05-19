<script type="text/javascript">
    function setColor(value,clas)
    {
        //to check if the selected
        var color = "";
        if (clas.includes("btnBigColore"))
        {
            color = "btnBigSelectColor";
        }
        else
        {
            color = "btnBigColore";
        }

        //to check which type (only for resume page)
        var type = "";
        if(clas.includes("query"))
        {
            type = "query";
        }
        else
        {
            type="account";
        }

        var allButtons = document.getElementsByClassName(clas);
        for(const btn of allButtons)
            {
                if(btn.value === value)
                {
                    //btn.style.background='#21D5D31F';
                    btn.classList.remove('btnBigColore');
                    btn.classList.add('btnBigSelectColor');
                }
                else
                {
                    btn.classList.add('btnBigColore');
                    btn.classList.remove('btnBigSelectColor');
                }
            }

            var allNextButtons = document.getElementsByClassName("list-group-item "+color+" "+type);
                    for(const btn of allNextButtons)
                        {
                            if(btn.value === value)
                            {
                                //btn.style.background='#21D5D31F';
                                btn.classList.remove('btnBigColore');
                                btn.classList.add('btnBigSelectColor');
                            }
                            else
                            {
                                btn.classList.add('btnBigColore');
                                btn.classList.remove('btnBigSelectColor');
                            }
                        }
    }
</script>