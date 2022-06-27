
function NavItem({name,to,label}){
    return (
        <li className='NavItem'>
                <a href={to} className='nav-link' >
                    <div className="nav-icon">
                        <i data-feather={name}></i>
                    </div>
                    <span>{label}</span>
                </a>
        </li>
    );
}